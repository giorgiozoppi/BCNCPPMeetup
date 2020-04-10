package main

import (
	"fmt"
	"net/http"
)

func contentType(url string) (string, error) {
	resp, err := http.Get(url)
	content := resp.Header.Get("Content-Type")
	defer resp.Body.Close()
	if err != nil {
		return "", nil
	}
	if content == "" {
		emptyContentError := fmt.Errorf("Content type no present")
		return "", emptyContentError
	}
	return content, err
}

func main() {
	res, err := contentType("https://www.libero.it")
	if err != nil {
		fmt.Println("Error fetching request")
	}
	fmt.Println(res)
}
