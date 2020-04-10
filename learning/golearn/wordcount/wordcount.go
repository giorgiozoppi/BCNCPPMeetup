package main

import (
	"fmt"
	"io/ioutil"
	"strings"
)

func main() {
	data, err := ioutil.ReadFile("./neruda.txt")
	poem := string(data)
	if err == nil {
		words := strings.Fields(poem)
		countMap := map[string]int{}
		for _, value := range words {
			countMap[strings.ToLower(value)]++
		}
		fmt.Println(countMap)
	}
}
