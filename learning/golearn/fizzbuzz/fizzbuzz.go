package main

import "fmt"

func main() {
	for i := 0; i < 100; i++ {
		number := i
		if number%15 == 0 {
			fmt.Printf("FizzBuzz %d\n", number)
		} else if number%3 == 0 {
			fmt.Printf("Fizz %d\n", number)
		} else if number%5 == 0 {
			fmt.Printf("Buzz %d\n", number)
		}
	}
}
