package main

import "fmt"

func secondMax(numbers ...uint32) (uint32, uint32) {
	var currentMax uint32
	var prevMax uint32
	for _, value := range numbers {
		if currentMax < value {
			prevMax = currentMax
			currentMax = value
		}
	}
	return currentMax, prevMax

}
func main() {
	numbers := []uint32{11, 7, 10, 36, 1, 39, 20, 55, 31}
	currentMax, prevMax := secondMax(numbers...)
	fmt.Printf("First max %d second max %d\n", currentMax, prevMax)
}
