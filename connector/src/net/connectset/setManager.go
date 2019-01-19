package net

import (
	"errors"
	"net/ws"
)

type SetManager struct {
	connectionSet    *ConnectionSet
	unloginConnectionSet *ConnectionSet
}

func (setManager *SetManager) InitSetManager(shardCount int)(err error){

	if shardCount < 0 {
		err = errors.New("shardCount must be int and more than 0")
		return
	}

	setManager.connectionSet = new(ConnectionSet)
	setManager.unloginConnectionSet = new(ConnectionSet)

	setManager.connectionSet.InitConnectionMap(shardCount, true)
	setManager.unloginConnectionSet.InitConnectionMap(shardCount, false)

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
