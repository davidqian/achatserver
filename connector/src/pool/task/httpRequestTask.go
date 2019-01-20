package pool

import (
	"fmt"
	"net/http"
)

type HttpRequestTask struct {
	Payload []byte
	Conn ConnectionAble
}

func (task *HttpRequestTask) DoTask(httpClient *http.Client)(err error){
	task.Conn.WriteMessage(task.Payload)
	fmt.Println("把数据原样返回了")
	return
}

