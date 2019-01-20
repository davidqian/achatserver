package ws

import (
	"errors"
	"github.com/gorilla/websocket"
	"net/http"
	"time"
)

// websocket server 实现 inet 接口
type WsServer struct {
	Addr string
	Pattern string
	server *http.Server
	setManager SetManagerAble
}

var(
	upgrader = websocket.Upgrader{
		CheckOrigin: func(r *http.Request) bool {
			return true //TODO：可以检查对应的 ref
		},
	}
)

func (ws *WsServer)ServeHTTP(w http.ResponseWriter, r *http.Request) {
	var(
		wsConn *websocket.Conn
		conn *Connection
		err error
	)
	if wsConn, err = upgrader.Upgrade(w, r, nil); err != nil {
		return
	}
	if conn, err = initConnection(wsConn, ws.setManager);err != nil{
		goto ERR
	}

	ws.setManager.AddConnection(conn)

	return
ERR:
	conn.Close()
	return
}

//TODO:可实现相应的 http server 配置
func (ws *WsServer)Start(setManager SetManagerAble)(err error) {

	ws.setManager = setManager
	mux := http.NewServeMux()
	mux.Handle(ws.Pattern, ws)
	ws.server = &http.Server{
		Addr: ws.Addr,
		WriteTimeout: time.Second * 3,
		Handler: mux,
	}

	if err = ws.server.ListenAndServe(); err != nil{
		err = errors.New("server can not start on " + ws.Addr)
	}
	return
}

func (ws *WsServer)Close(){
	ws.server.Close()
}