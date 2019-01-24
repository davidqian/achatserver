package main

import "fmt"

func add(a, b int)(err error){
	fmt.Println("hello")
	return
}

func callback(y int, f func(int, int)(error)){
	f(y, 2)
}

func main(){
	callback(1, add)
}
