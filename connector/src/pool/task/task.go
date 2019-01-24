package pool

import (
	"net/http"
	"protocol"
)

type Task struct {
	Payload []byte
	Protocol *protocol.Protocol
	Conn ConnectionAble
}

func (task *Task) DoTask(httpClient *http.Client)(err error){
	fun := GetHandlerByOperate(task.Protocol.Operate)
	err = fun(httpClient, task)
	return
}

func MakeTaskFromPayload(payload []byte, conn ConnectionAble)(task *Task){
	proto := &protocol.Protocol{}
	proto.Decode(payload)
	task = &Task{
		Payload: payload,
		Protocol: proto,
		Conn: conn,
	}
	return
}

func MakeTaskFromParam(version uint16, operate uint32, seqId uint32, body string, conn ConnectionAble)(task *Task, err error){
	proto := &protocol.Protocol{
		Version: version,
		Operate: operate,
		SeqId: seqId,
		Body: body,
	}
	if payload, err := proto.Encode(); err == nil{
		task = & Task{
			Payload: payload,
			Protocol: proto,
			Conn: conn,
		}
	}
	return
}
