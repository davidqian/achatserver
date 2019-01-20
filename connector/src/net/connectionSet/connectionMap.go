package net

import (
	"net/ws"
	"pool"
	"sync"
)

type ConnectionMap struct {
	chanNeed bool
	connections map[string]*ws.Connection
	rwMutex *sync.RWMutex
	reqWorkFollow *pool.WorkFlow
	resWorkFollow *pool.WorkFlow
}

func InitConnectionMap(chanNeed bool) (connectionMap *ConnectionMap){
	connectionMap = &ConnectionMap{
		chanNeed : chanNeed,
		connections : make(map[string]*ws.Connection),
		rwMutex : new(sync.RWMutex),
	}
	if(chanNeed){
		//TODO:需要提取参数
		connectionMap.reqWorkFollow = pool.NewWorkFlow(10, 10)
		connectionMap.resWorkFollow = pool.NewWorkFlow(10, 10)
	}
	return
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

func (m *ConnectionMap) AddRequestTask(task pool.TaskAble){
	m.reqWorkFollow.AddTask(task)
}

func (m *ConnectionMap) AddResponseTask(task pool.TaskAble){
	m.resWorkFollow.AddTask(task)
}

//TODO: 这里需要增加 timer，检查心跳

