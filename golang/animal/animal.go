package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type Animal struct {
	food       string
	locomotion string
	noise      string
}

func (a Animal) Eat() string {
	return a.food
}
func (a Animal) Move() string {
	return a.locomotion
}
func (a Animal) Speak() string {
	return a.noise
}
func NewAnimal(f string, l string, n string) Animal {
	return Animal{
		locomotion: l,
		food:       f,
		noise:      n,
	}
}
func main() {
	animals := map[string]Animal{
		"cow":   NewAnimal("grass", "walk", "moo"),
		"bird":  NewAnimal("worms", "fly", "peep"),
		"snake": NewAnimal("mice", "slither", "hsss"),
	}
	for {

		fmt.Printf(">")
		scan := bufio.NewScanner(os.Stdin)
		scan.Scan()
		fmt.Printf(">")
		animal := strings.TrimSpace(scan.Text())
		tmp, ok := animals[animal]
		if !ok {
			fmt.Println("Animal not found")
			continue
		}
		scan.Scan()
		action := strings.TrimSpace(scan.Text())

		switch action {
		case "eat":
			{
				fmt.Println(tmp.Move())
			}
		case "move":
			{
				fmt.Println(tmp.Move())

			}
		case "speak":
			{
				fmt.Println(tmp.Speak())

			}
		default:
			fmt.Printf("Action not valid")
		}
	}
}
