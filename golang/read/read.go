package main

/**
Write a program which reads information from a file and represents it in a slice of structs.
Assume that there is a text file which contains a series of names.
Each line of the text file has a first name and a last name, in that order,
separated by a single space on the line.
Your program will define a name struct which has two fields,
fname for the first name, and lname for the last name.
Each field will be a string of size 20 (characters).Your program should prompt
the user for the name of the text file.
Your program will successively read each line of the text file and create a struct
which contains the first and last names found in the file.
Each struct created will be added to a slice,
and after all lines have been read from the file,
your program will have a slice containing one struct for each line in the file.
After reading all lines from the file,
your program should iterate through your slice of structs and print the first and last names
found in each struct.
**/
import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type Person struct {
	FName string
	LName string
}

func readFile(fileName string) ([]Person, error) {

	fp, err := os.Open(fileName)

	if err != nil {
		return nil, err
	}
	defer fp.Close()
	person := make([]Person, 0)
	scanner := bufio.NewScanner(fp)
	for scanner.Scan() {
		names := strings.Split(scanner.Text(), " ")
		person = append(person, Person{
			FName: names[0],
			LName: names[1],
		})

	}
	return person, nil
}
func main() {
	fmt.Print("Names file name:")
	mainScanner := bufio.NewScanner(os.Stdin)
	mainScanner.Scan()
	fileName := mainScanner.Text()
	personList, err := readFile(fileName)
	if err != nil {
		fmt.Printf("Errors %v\n", err)
		return
	}
	for _, p := range personList {
		fmt.Printf("%v %v\n", p.FName, p.LName)
	}
}
