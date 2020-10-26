package main

import "fmt"

func main() {
	colors := []string{"Red", "Blue", "Green", "Yellow", "Pink", "White", "Black"}
	sliced_colors := colors[2:4]
	for k, color := range sliced_colors {
		fmt.Printf("%d -> %s\n", k, color)
	}
}
