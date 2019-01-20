package pool

type Worker struct {
	Workbench       chan TaskAble
	GWorkbenchQueue chan chan TaskAble
	Stoped        chan bool
}

// 新建一个工作台
func NewWorker(WorkbenchQueue chan chan TaskAble) *Worker {
	return &Worker{
		Workbench:       make(chan TaskAble),
		GWorkbenchQueue: WorkbenchQueue,
		Stoped:        make(chan bool),
	}
}

//开始工作
func (w *Worker) Start() {
	go func() {
		for {
			w.GWorkbenchQueue <- w.Workbench
			select {
			case taskAble := <-w.Workbench:
				taskAble.DoTask()
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