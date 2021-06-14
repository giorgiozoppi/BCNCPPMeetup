package main

import (
	"fmt"
)

func main() {
	var number float64
	fmt.Scan(&number)
	intValue := int(number)
	fmt.Printf("%d\n", intValue)
	
}
