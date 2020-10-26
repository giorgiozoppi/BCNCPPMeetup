package main

import "fmt"

func main() {
	stocks := map[string]float32{
		"MSFT": 90.23,
		"HPQ":  23.21,
		"OCL":  50.12,
	}
	for company, value := range stocks {
		fmt.Printf("%s %v\n", company, value)
	}
}
