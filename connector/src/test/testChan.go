package main

import "fmt"

type chanTest struct {
	num int
}

type chanSet struct {
	chans chan *chanTest
}

func (cs *chanSet) writeLoop(){
	fmt.Println("write begin")
	cs.chans <- new(chanTest)
	fmt.Println("write end")
}

func main(){
	var(
		i int
		cs *chanSet
	)
	cs = new(chanSet)
	cs.chans = make(chan *chanTest, 10)

	for i = 0; i < 100; i++  {
		go cs.writeLoop()
	}
}
