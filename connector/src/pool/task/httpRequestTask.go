package pool

//调用 http 协议处理请求
type HttpRequestTask struct {
	payload []byte
	cbObject interface{} //TODO：这里需要提取出去
}

func (task *HttpRequestTask) DoTask()(err error){
	//发送 http 请求处理业务逻辑
	return
}

