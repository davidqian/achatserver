package net

import (
	"net/ws"
	"pool"
	"util"
)

type ConnectionSet struct {

	shardCount int
	shards []*ConnectionMap
}

func (sm *ConnectionSet) GetShardCount()(shardCount int){
	return sm.shardCount
}

func InitConnectionSet(shardCount int, chanNeed bool)(connectionSet *ConnectionSet){
	var(
		i int
		m *ConnectionMap
	)
	connectionSet = &ConnectionSet{
		shardCount:shardCount,
		shards:make([]*ConnectionMap, 0, shardCount-1),
	}
	for i = 0 ; i < shardCount; i++{
		m = InitConnectionMap(chanNeed)
		connectionSet.shards = append(connectionSet.shards, m)
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

func (sm *ConnectionSet) addInTask(key string, task pool.TaskAble){
	var(
		m *ConnectionMap
	)
	m = sm.GetMap(key)
	m.AddRequestTask(task)
	return
}

func (sm *ConnectionSet) addOutTask(key string, task pool.TaskAble){
	var(
		m *ConnectionMap
	)
	m = sm.GetMap(key)
	m.AddResponseTask(task)
	return
}

func (sm *ConnectionSet) GetMap(key string)(m *ConnectionMap){
	m = sm.shards[util.BkdrHash(key)&uint32((sm.shardCount-1))]
	return
}

