package pool

import (
	"net/http"
)

func InitHandler(httpClient *http.Client, task *Task)(err error){
	//TODO: 验证重复登录,加锁处理比较容易，如果机器宕机可能会导致一段时间用户连接不上
	//TODO: 去用户模块验证登录
	//TODO: 为 connection 赋值
	//TODO: 改变客户端状态
	err = task.Conn.WriteMessage([]byte("have inited"))
	return
}

