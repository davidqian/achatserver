package pool

import "net/http"

type Worker struct {
	Workbench       chan TaskAble
	GWorkbenchQueue chan chan TaskAble
	Stoped        chan bool
	httpClient *http.Client
}

// 新建一个工作台
func NewWorker(WorkbenchQueue chan chan TaskAble) (work *Worker) {
		work = &Worker{
			Workbench:       make(chan TaskAble),
			GWorkbenchQueue: WorkbenchQueue,
			Stoped:        make(chan bool),
		}

		//TODO：抽离配置
		tr := &http.Transport{
			MaxIdleConns: 100,
			MaxIdleConnsPerHost: 2,
		}

		work.httpClient = &http.Client{
			Transport: tr,
		}
		return
}

//开始工作
func (w *Worker) Start() {
	go func() {
		for {
			w.GWorkbenchQueue <- w.Workbench
			select {
			case taskAble := <-w.Workbench:
				taskAble.DoTask(w.httpClient)
			case stoped := <-w.Stoped:
				if true == stoped {
					return
				}
			}
		}
	}()
}

func (w *Worker) Stop() {
	go func() {
		w.Stoped <- true
	}()
}
