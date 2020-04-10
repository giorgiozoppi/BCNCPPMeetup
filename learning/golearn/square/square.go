package main

import (
	"fmt"
)

type Point struct {
	x int
	y int
}
type Square struct {
	Center Point
	Length int
}

func (s *Square) Move(dx int, dy int) {
	s.Center.x = s.Center.x + dx
	s.Center.y = s.Center.y + dy
}
func (s *Square) Area() int {
	return s.Length * s.Length
}
func NewSquare(x int, y int, length int) (*Square, error) {
	if length <= 0 {
		return nil, fmt.Errorf("Invalid length")
	}

	square := Square{
		Center: Point{x, y},
		Length: length}
	return &square, nil
}
func main() {
	square, err := NewSquare(10, 10, 10)
	if err != nil {
		fmt.Println("invalid square")
	}
	fmt.Printf("area %d\n", square.Area())
	square.Move(20, 20)
	fmt.Printf("position %d, position %d\n", square.Center.x, square.Center.y)

}
