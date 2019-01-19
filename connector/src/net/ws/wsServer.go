package ws

import (
	"errors"
	"github.com/gorilla/websocket"
	"net/connectset"
	"net/http"
	"time"
)

// websocket server 实现 inet 接口
type WsServer struct {
	Addr string
	Pattern string
	server *http.Server
	setManager *net.SetManager
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
		wxConn *websocket.Conn
		conn *Connection
		err error
	)
	if wxConn, err = upgrader.Upgrade(w, r, nil); err != nil {
		return
	}

	if conn, err = initConnection(wxConn, ws.setManager);err != nil{
		goto ERR
	}

	ws.setManager.AddConnection(conn)

ERR:
	conn.Close()
}

//TODO:可实现相应的 http server 配置
func (ws *WsServer)Start(shardCount int)(err error) {

	ws.setManager = new(net.SetManager)
	if err = ws.setManager.InitSetManager(shardCount);err != nil{
		return
	}

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