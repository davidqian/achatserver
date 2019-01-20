package main

type testInstantiation struct {
	A int
}

func (ti *testInstantiation) test(){
	println(ti.A)
}

func (ti *testInstantiation) addA(){
	ti.A += 1
}

func main(){
	var ti testInstantiation
	ti.addA()
	ti.test()

	var ti1 testInstantiation
	ti1.test()
}
