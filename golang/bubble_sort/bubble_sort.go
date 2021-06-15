package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func Swap(numbers []int, i int) {
	numbers[i], numbers[i+1] = numbers[i+1], numbers[i]
}
func BubbleSort(numbers []int) {
	swapped := false
	for {
		swapped = false
		for j := 0; j < len(numbers)-1; j++ {
			if numbers[j] > numbers[j+1] {
				Swap(numbers, j)
				swapped = true
			}
		}
		if !swapped {
			break
		}
	}
}
func main() {
	numbers := make([]int, 0)

	for i := 0; i < 10; i++ {
		scanner := bufio.NewScanner(os.Stdin)
		scanner.Scan()
		txt := scanner.Text()
		num, err := strconv.Atoi(txt)
		if err != nil {
			panic("Error converting value")
		}
		numbers = append(numbers, num)
	}
	BubbleSort(numbers)
	for i := range numbers {
		fmt.Printf("%d ", numbers[i])
	}
	fmt.Printf("\n")

}
