/**
Write a program to sort an array of integers.
The program should partition the array into 4 parts,
each of which is sorted by a different goroutine.
Each partition should be of approximately equal size.
Then the main goroutine should merge the 4 sorted subarrays into one large sorted array.
The program should prompt the user to input a series of integers.
Each goroutine which sorts ¼ of the array should print the subarray that it will sort.
When sorting is complete, the main goroutine should print the entire sorted list.
**/
package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
	"sync"
)

func ScanIntegers() []int {
	vector := make([]int, 0)
	for {
		fmt.Printf("Digit a number or 'X'>")
		scanner := bufio.NewScanner(os.Stdin)
		scanner.Scan()
		text := strings.TrimSpace(scanner.Text())
		if len(text) == 0 {
			fmt.Printf("Invalid data")
			continue
		}
		if text[0] == 'X' {
			break
		}
		v, err := strconv.Atoi(text)
		if err != nil {
			fmt.Printf("bad number")
			continue
		}
		vector = append(vector, v)
	}
	return vector
}
func PartialSort(array sort.IntSlice, channel chan []int, wg *sync.WaitGroup) {
	defer wg.Done()
	fmt.Printf("Subarray to be sorted: %v\n", array)
	array.Sort()
	fmt.Printf("Subarray sorted: %v\n", array)
	channel <- array
}
func MergePart(channel chan []int, outchan chan []int, wg *sync.WaitGroup) {
	first := <-channel
	second := <-channel
	fmt.Printf("Subarray #1 to be marged: %v\n", first)
	fmt.Printf("Subarray #2 to be marged: %v\n", second)
	tmp := make([]int, len(first)+len(second))
	defer wg.Done()
	j, i, k := 0, 0, 0
	for {
		if i < len(first) && j < len(second) {

			if first[i] <= second[j] {
				tmp[k] = first[i]
				i++
				k++
			} else if first[i] > second[j] {
				tmp[k] = second[j]
				j++
				k++
			} else if first[i] == second[j] {
				tmp[k] = first[i]
				tmp[k+1] = second[j]
				i++
				j++
				k = k + 2
			}
		} else {
			for ; i < len(first); i++ {
				tmp[k] = first[i]
				k++
			}
			for ; j < len(second); j++ {
				tmp[k] = second[j]
				k++
			}
			break
		}
	}
	fmt.Printf("Merged array = %v\n", tmp)
	outchan <- tmp
}
func main() {
	var wg sync.WaitGroup
	ch := make(chan []int, 4)
	array := ScanIntegers()
	quarter := len(array) / 4
	fmt.Printf("Quarter %v %v\n", quarter, len(array))
	aux := make([]int, 1)
	count := 0
	var part sort.IntSlice
	for sum := 0; sum < len(array); sum += quarter {
		upperbound := sum + quarter
		if count < 3 {
			part = array[sum:upperbound]
		} else {
			part = array[sum:]
			sum = len(array)
			upperbound = sum
		}
		count++
		if len(part) > 0 {
			wg.Add(1)
			go PartialSort(part, ch, &wg)
		}
	}
	partialch := make(chan []int, 2)
	resultch := make(chan []int, 1)
	wg.Add(1)
	go MergePart(ch, partialch, &wg)
	wg.Add(1)
	go MergePart(ch, partialch, &wg)
	wg.Add(1)
	go MergePart(partialch, resultch, &wg)
	wg.Wait()
	aux = <-resultch
	fmt.Printf("Sorted Array: %v\n", aux)

}
