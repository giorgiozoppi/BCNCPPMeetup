package main

import "fmt"

func main() {
	colors := []string{"Red", "Blue", "Green", "Yellow", "Pink", "White", "Black"}
	for k, color := range colors {
		fmt.Printf("%d -> %s\n", k, color)
	}
}
