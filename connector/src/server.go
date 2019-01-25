package main

import (
	"log"
	"net/connectionSet"
	"net/ws"
	"pool/task"
)

func startWs(){
	var(
		server *ws.WsServer
		err error
		setManager ws.SetManagerAble
	)

	//初始化
	pool.InitHandlerInstance()
	pool.InitMapHandler()

	server = &ws.WsServer{
		Addr: "0.0.0.0:8888",
		Pattern: "/ws",
	}

	setManager = net.InitSetManager(10)
	if err = server.Start(setManager); err != nil{
		log.Fatal(err)
	}
}

func main() {
	startWs()
}
