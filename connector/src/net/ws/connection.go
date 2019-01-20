package ws

import (
	"errors"
	"github.com/gorilla/websocket"
	"github.com/satori/go.uuid"
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
	setManager SetManagerAble
}

type SetManager interface {

}

func initConnection(wsConn *websocket.Conn, setManger SetManagerAble)(conn *Connection, err error){
	var(
		newUuiId uuid.UUID
	)
	conn = &Connection{
		wsConn: wsConn,
		inChan: make(chan []byte, 100),
		outChan: make(chan []byte, 100),
		closeChan: make(chan byte, 1),
		setManager:setManger,
	}

	if newUuiId, err = uuid.NewV4(); err != nil{
		goto ERR
	}

	conn.Uuid = newUuiId.String()

	//启动读协程
	go conn.readLoop()

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
}

func (conn *Connection) ReadMessage()(data[]byte, err error){
	select {
	case data = <- conn.inChan:
	case <- conn.closeChan:
		err = errors.New("connection is closed!")
	}
	return
}

//TODO:直接把数据给网卡，是否需要考虑合包？
func (conn *Connection) WriteMessage(data []byte)(err error){
	if conn.isClose {
		err = errors.New("socket is closed!")
	}
	if err = conn.wsConn.WriteMessage(websocket.TextMessage,data); err != nil{
		goto ERR
	}
ERR:
	conn.Close()
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

