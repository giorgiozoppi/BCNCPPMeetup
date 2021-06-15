package main

import (
	"fmt"
	"strconv"
)

func Swap(slice []int, i int) {
	slice[i], slice[i+1] = slice[i+1], slice[i]
}
func BubbleSort(nums []int) []int {
	for i := 0; i < len(nums)-1; i++ {
		for j := 0; j < len(nums)-i-1; j++ {
			if nums[j] > nums[j+1] {
				Swap(nums, j)
			}
		}
	}
	return nums
}

func main() {
	//nums := []int{3, 5, 2, 7, 2, 4, 1, 5, 7}
	nums := make([]int, 0, 10)

	for len(nums) <= 10 {
		var tempString string
		fmt.Print("Input number (Type X to stop): ")
		fmt.Scan(&tempString)

		if tempString == "X" {
			break
		}
		tempInt, err := strconv.Atoi(tempString)
		if err != nil {
			fmt.Println("Error")
			break
		}

		nums = append(nums, tempInt)
		fmt.Println("Numbers: ", nums)
	}

	fmt.Println("Before sort: ", nums)
	BubbleSort(nums)
	fmt.Println("After sort: ", nums)
}
