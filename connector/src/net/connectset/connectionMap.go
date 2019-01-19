package net

import (
	"net/ws"
	"sync"
)

type ConnectionMap struct {
	chanNeed bool
	connections map[string]*ws.Connection
	rwMutex *sync.RWMutex
	request chan chan []byte
	response chan chan []byte
}

func (m *ConnectionMap) addConnection(key string, con *ws.Connection){
	m.rwMutex.Lock()
	m.connections[key] = con
	m.rwMutex.Unlock()
}

func (m *ConnectionMap) removeConnection(key string) {
	m.rwMutex.Lock()
	delete(m.connections, key)
	m.rwMutex.Unlock()
}

func (m *ConnectionMap) getConnection(key string)(con *ws.Connection){
	m.rwMutex.RLock()
	con = m.connections[key]
	m.rwMutex.RUnlock()
	return
}

//TODO: 这里需要增加 timer，检查心跳

//TODO：这里需要增加http请求处理的 chan 池

//TODO: 这里需要增加返回数据的 chan 池


