package pool

import (
	"log"
	"os"
	"os/signal"
	"syscall"
)

type Dispatcher struct {
	MaxWorkers      int
	Workers         []*Worker
	Closed          chan bool
	EndDispatch     chan os.Signal
	GTaskQueue      chan TaskAble
	GWorkbenchQueue chan chan TaskAble
}

func NewDispatcher(maxWorkers, maxQueue int) *Dispatcher {
	Closed := make(chan bool)
	EndDispatch := make(chan os.Signal)
	TaskQueue := make(chan TaskAble, maxQueue)
	WorkbenchQueue := make(chan chan TaskAble, maxWorkers)
	signal.Notify(EndDispatch, syscall.SIGINT, syscall.SIGTERM)
	return &Dispatcher{
		MaxWorkers:      maxWorkers,
		Closed:          Closed,
		EndDispatch:     EndDispatch,
		GTaskQueue:      TaskQueue,
		GWorkbenchQueue: WorkbenchQueue,
	}
}

func (d *Dispatcher) Run() {
	for i := 0; i < d.MaxWorkers; i++ {
		worker := NewWorker(d.GWorkbenchQueue)
		d.Workers = append(d.Workers, worker)
		worker.Start()
	}
	go d.Dispatch()
}

func (d *Dispatcher) Dispatch() {
FLAG:
	for {
		select {
		case endDispatch := <-d.EndDispatch:
			log.Printf("收到系统指令[%v]...\n", endDispatch)
			close(d.GTaskQueue)
			log.Printf("已关闭工作台\n")
		case taskAble, Ok := <-d.GTaskQueue:
			if true == Ok {
				go func(taskAble TaskAble) {
					Workbench := <-d.GWorkbenchQueue
					Workbench <- taskAble
				}(taskAble)
			} else {
				for _, w := range d.Workers {
					w.Stop()
				}
				d.Closed <- true
				break FLAG
			}
		}
	}
}
