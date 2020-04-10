package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func killServer(pidFile string) error {
	currentFile, err := os.Open(pidFile)
	if err != nil {
		return err
	}
	defer currentFile.Close()

	fileScanner := bufio.NewScanner(currentFile)
	fileScanner.Split(bufio.ScanLines)
	fileScanner.Scan()
	lineValue := fileScanner.Text()
	pidValue, err := strconv.Atoi(lineValue)
	if err != nil {
		return err
	}
        os.Kill(pidValue)	
	return nil
}
func main() {
	err := killServer("/home/jozoppi/pidfile")
	if err != nil {
		panic("No pid file")
	}
}
