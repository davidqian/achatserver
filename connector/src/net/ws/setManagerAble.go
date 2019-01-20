package ws

import "pool"

type SetManagerAble interface {
	AddConnection(con *Connection)
	RemoveConnection(con *Connection)
	AddRequestTask(task pool.TaskAble, conn *Connection)
}
