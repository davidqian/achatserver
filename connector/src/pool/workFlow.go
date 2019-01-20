package pool

import "log"

type WorkFlow struct {
	GDispatch *Dispatcher
}

func NewWorkFlow(maxWorkers, maxQueue int)(workFlow *WorkFlow){
	workFlow = &WorkFlow{}
	workFlow.StartWorkFlow(maxWorkers, maxQueue)
	return
}

func (wf *WorkFlow) StartWorkFlow(maxWorkers, maxQueue int) {
	wf.GDispatch = NewDispatcher(maxWorkers, maxQueue)
	wf.GDispatch.Run()
}

func (wf *WorkFlow) AddTask(taskAble TaskAble) {
	wf.GDispatch.GTaskQueue <- taskAble
}

func (wf *WorkFlow) CloseWorkFlow() {
	closed := <-wf.GDispatch.Closed
	if true == closed {
		log.Println("调度器(流水线)已关闭.")
	}
}
