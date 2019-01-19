package net

import (
	"net/ws"
	"sync"
	"util"
)

type ConnectionSet struct {

	shardCount int
	shards []*ConnectionMap
}

func (sm *ConnectionSet) GetShardCount()(shardCount int){
	return sm.shardCount
}

func (sm *ConnectionSet) InitConnectionMap(shardCount int, chanNeed bool){
	var(
		i int
		m *ConnectionMap
	)
	sm.shardCount = shardCount
	sm.shards = make([]*ConnectionMap, 0, shardCount-1)
	for i = 0 ; i < shardCount; i++{
		m = new(ConnectionMap)
		m.chanNeed = chanNeed
		m.connections = make(map[string]*ws.Connection)
		m.rwMutex = new(sync.RWMutex)
		sm.shards = append(sm.shards, m)
	}
	return
}

func (sm *ConnectionSet) addConnection(key string, con *ws.Connection){
	var(
		m *ConnectionMap
	)
	m = sm.GetMap(key)
	m.addConnection(key, con)
}

func (sm *ConnectionSet) removeConnection(key string){
	var(
		m *ConnectionMap
	)
	m = sm.GetMap(key)
	m.removeConnection(key)
}

func (sm *ConnectionSet) getConnection(key string)(con *ws.Connection){
	var(
		m *ConnectionMap
	)
	m = sm.GetMap(key)
	con = m.getConnection(key)
	return
}

func (sm *ConnectionSet) GetMap(key string)(m *ConnectionMap){
	m = sm.shards[util.BkdrHash(key)&uint32((sm.shardCount-1))]
	return
}