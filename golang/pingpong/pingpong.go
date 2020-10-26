package main
import ("fmt" 
		"sync")

func main() {
	outCh:=make(chan string)
	go func() {
		for i:=0; i< 10; i++ {
		outCh <- "Ping"
		fmt.Println("Ping")
		}
		outCh<-"Poison"
	} () 
	for j:=0; j < 10;j++ {
		inValue:=<-outCh
		if (inValue == "Ping") {
		fmt.Println("Pong")		
	}
	}
}