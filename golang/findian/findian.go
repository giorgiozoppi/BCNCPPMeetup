package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

const (
	NLetter = 'n'
	ILetter = 'i'
	ALetter = 'a'
)

func main() {
	var sentence string
	fmt.Print("Write a sentence: ")
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	sentence = strings.ToLower(scanner.Text())
	if sentence[0] != ILetter || sentence[len(sentence)-1] != NLetter {
		fmt.Println("Not Found!")
	} else if strings.ContainsRune(sentence, ALetter) {
		fmt.Println("Found!")

	} else {
		fmt.Println("Not Found!")
	}
}
