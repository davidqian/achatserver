package net

import (
	"net/ws"
	"pool"
)

type SetManager struct {
	connectionSet    *ConnectionSet
	unloginConnectionSet *ConnectionSet
}

func InitSetManager(shardCount int)(setManager *SetManager){

	setManager = &SetManager{}

	setManager.connectionSet = InitConnectionSet(shardCount, true)
	setManager.unloginConnectionSet = InitConnectionSet(shardCount, true)

	return
}

func (setManager *SetManager) AddConnection(con *ws.Connection){

		if con.UniqueId != "" {

			setManager.connectionSet.addConnection(con.UniqueId, con)

			setManager.unloginConnectionSet.removeConnection(con.Uuid)

		} else {

			setManager.unloginConnectionSet.addConnection(con.Uuid, con)

		}
}

func (setManager *SetManager) RemoveConnection(con *ws.Connection){

		if con.UniqueId != "" {
			setManager.connectionSet.removeConnection(con.UniqueId)
		} else {
			setManager.unloginConnectionSet.removeConnection(con.Uuid)
		}
}
//TODO: 这两个函数存在一个问题,如果在放 task 的时候，链接所在 set 发生了转移，在那层解决？
//TODO: 在登录协议走完之前，不做任何的推送未登录状态的请求处理和推送，用协议保证顺序
func (setManager *SetManager) AddRequestTask(task pool.TaskAble, con *ws.Connection){
		if con.UniqueId != "" {
			setManager.connectionSet.addInTask(con.UniqueId, task)
		} else {
			setManager.unloginConnectionSet.addInTask(con.Uuid, task)
		}
}

func (setManager *SetManager) AddResponseTask(task pool.TaskAble, con *ws.Connection){
	if con.UniqueId != "" {
		setManager.connectionSet.addInTask(con.UniqueId, task)
	} else {
		setManager.unloginConnectionSet.addInTask(con.Uuid, task)
	}
}
