package ws

import (
	"errors"
	"github.com/gorilla/websocket"
	"github.com/satori/go.uuid"
	"net/connectset"
	"sync"
)

type Connection struct {
	UniqueId string //全局唯一 id， 用用户 id 做为唯一标识
	Uuid string  //未登录时使用

	wsConn *websocket.Conn
	inChan chan []byte
	outChan chan []byte
	closeChan chan byte
	mutex sync.Mutex
	isClose bool
	setManager * net.SetManager
}

func initConnection(wsConn *websocket.Conn, setManager *net.SetManager)(conn *Connection, err error){
	var(
		tmpId uuid.UUID
	)
	conn = &Connection{
		wsConn: wsConn,
		inChan: make(chan []byte, 1000),
		outChan: make(chan []byte, 1000),
		closeChan: make(chan byte, 1),
		setManager:setManager,
	}

	if tmpId, err = uuid.NewV4(); err != nil{
		goto ERR
	}

	conn.Uuid = tmpId.String()

	//启动读协程
	go conn.readLoop()
	//启动写协程
	//TODO: 需要看看是否还需要，需要看网卡能力？
	go conn.writeLoop()

ERR:
	err = errors.New("uuid error")

	return
}

func (conn *Connection)Close(){
	//线程安全的，可重入的
	conn.wsConn.Close()

	//closeChan 关闭只能被调用一次
	conn.mutex.Lock()
	if !conn.isClose {
		close(conn.closeChan)
		conn.isClose = true
	}
	conn.mutex.Unlock()

	conn.setManager.RemoveConnection(conn)
}

func (conn *Connection) ReadMessage()(data[]byte, err error){
	select {
	case data = <- conn.inChan:
	case <- conn.closeChan:
		err = errors.New("connection is closed!")
	}
	return
}

func (conn *Connection) WriteMessage(data []byte)(err error){
	select {
	case conn.outChan <- data:
	case <- conn.closeChan:
		err = errors.New("connection is closed!")
	}
	return
}

//TODO: 如何把请求转给其它模块，是否需要保证请求并行？如何保证？
func (conn *Connection) readLoop(){
	var(
		data []byte
		err error
	)
	for{
		if _,data,err = conn.wsConn.ReadMessage();err != nil{
			goto ERR
		}
		select {
		case conn.inChan <- data:
		case <- conn.closeChan:
			goto ERR
		}
	}
ERR:
	conn.Close()
}

//TODO: 如何合并包？
func (conn *Connection) writeLoop(){
	var(
		data []byte
		err error
	)
	for{
		select {
		case data = <- conn.outChan:
		case <- conn.closeChan:
			goto ERR
		}
		if err = conn.wsConn.WriteMessage(websocket.TextMessage,data); err != nil{
			goto ERR
		}
	}
ERR:
	conn.Close()
}