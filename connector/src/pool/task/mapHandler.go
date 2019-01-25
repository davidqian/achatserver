package pool

import (
"net/http"
)

var instance *MapHandler

type MapHandler struct {
	HandlerMap map[uint32]func(*http.Client, *Task)(error)
}

func InitHandlerInstance()(mapHandler *MapHandler){
	if(instance == nil){
		instance = &MapHandler{
			HandlerMap:make(map[uint32]func(*http.Client, *Task)(error)),
		}
	}
	return
}

func InitMapHandler(){
	//解析 handler 包，处理函数
	SetHandlerByOperate(1, InitHandler)
}

func GetHandlerByOperate(operate uint32)(fun func(*http.Client, *Task)(error)){
	fun = instance.HandlerMap[operate]
	return
}

func SetHandlerByOperate(operate uint32, fun func(*http.Client, *Task)(error)){
	instance.HandlerMap[operate] = fun
}


