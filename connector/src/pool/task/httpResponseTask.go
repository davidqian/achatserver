package pool

import (
	"net/http"
)

type HttpResponseTask struct {
	Payload []byte
	Conn ConnectionAble
}

func (task *HttpResponseTask) DoTask(httpClient *http.Client)(err error){
	//发送 http 返回数据
	return
}