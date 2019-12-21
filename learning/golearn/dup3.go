package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"strings"
)

func main() {
	counts := make(map[string]int)
	filemap := make(map[string][]string)
	filenames := make(map[string]int)
	for _, filename := range os.Args[1:] {
		data, err := ioutil.ReadFile(filename)
		if err != nil {
			fmt.Fprintf(os.Stderr, "dup3: %v\n", err)
			continue
		}
		for _, line := range strings.Split(string(data), "\n") {
			if len(line) > 1 {
				counts[line]++
				if counts[line] > 1 {
				        hashkey:= line+"."+filename
					if filenames[hashkey] == 0 {
						filemap[line] = append(filemap[line], filename)
						filenames[hashkey]++
					}
				}
			}
		}
	}

	for line, key := range filemap {
		fmt.Printf("%s->%s:%d \n", line, key, counts[line])
	}

}
