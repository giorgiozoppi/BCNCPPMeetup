package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

func readInt(reader *bufio.Reader) (int, bool, error) {
	var number int
	input, err := reader.ReadString('\n')
	if err != nil {
		return 0, false, err
	}
	data := strings.ToUpper(input[0 : len(input)-1])
	if data == "X" {
		return 0, true, nil
	}
	number, err = strconv.Atoi(data)
	if err != nil {
		return 0, false, err
	}
	return number, false, nil
}
func main() {
	reader := bufio.NewReader(os.Stdin)
	fmt.Print("Digit slice size: ")
	sliceSize, _, err := readInt(reader)
	if err != nil {
		fmt.Println("invalid slice size")
	}
	intSlice := make(sort.IntSlice, sliceSize)
	currentSize := 0
	for {
		fmt.Print("Insert number: ")
		number, stopAsking, err := readInt(reader)
		if err != nil {
			fmt.Println("Error reading number")
		} else if stopAsking {
			fmt.Println("OK. You pressed X")
			return
		} else {
			if currentSize >= sliceSize {
				intSlice = append(intSlice, number)
			} else {
				intSlice[0] = number
			}
			currentSize++
		}
		intSlice.Sort()
		fmt.Printf("Ordered slice %v\n", intSlice)
	}
}
