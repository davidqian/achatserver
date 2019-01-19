package main

import "fmt"

type SyncMap struct {

	shardCount int
	shards [] *test
}

type test struct {
	num int
}

func main()  {
	var(
		i int
		sm *SyncMap
		t *test
	)
	sm = new(SyncMap)
	sm.shards = make([]*test, 0, 2)
	for i = 0; i < 3; i++ {
		t = new(test)
		t.num = i
		sm.shards = append(sm.shards, t)
	}
	fmt.Println(sm.shards[1].num)
}
