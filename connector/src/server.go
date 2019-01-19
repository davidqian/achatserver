package main

import (
	"log"
	"net/ws"
)

func startWs(){
	var(
		server *ws.WsServer
		err error
	)

	server = &ws.WsServer{
		Addr: "0.0.0.0:8888",
		Pattern: "/ws",
	}

	if err = server.Start(100); err != nil{
		log.Fatal(err)
	}
}

func main() {
	startWs()
}
