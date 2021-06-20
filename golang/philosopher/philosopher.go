/*There should be 5 philosophers sharing chopsticks, with one chopstick
between each adjacent pair of philosophers.
Each philosopher should eat only 3 times (not in an infinite loop as we did in lecture)
The philosophers pick up the chopsticks in any order, not lowest-numbered first
(which we did in lecture).
In order to eat, a philosopher must get permission from a host which executes in its own goroutine.
The host allows no more than 2 philosophers to eat concurrently.
Each philosopher is numbered, 1 through 5.
When a philosopher starts eating (after it has obtained necessary locks)
it prints “starting to eat <number>” on a line by itself,
where <number> is the number of the philosopher.
When a philosopher finishes eating (before it has released its locks)
it prints “finishing eating <number>” on a line by itself, where <number> is the number of the philosopher.
*/
package main

import (
	"fmt"
	"math/rand"
	"sync"
)

type ChopStick struct{ sync.Mutex }
type Philo struct {
	Id        int
	LeftChop  *ChopStick
	RightChop *ChopStick
}

func (p *Philo) Eat(start chan bool, h chan bool, wg *sync.WaitGroup) {
	i := <-start
	fmt.Printf("Philo %v, Active %v\n", p.Id, i)
	defer wg.Done()

	for i := 0; i < 3; i++ {
		p.LeftChop.Lock()
		p.RightChop.Lock()
		fmt.Printf("start to eat %v\n", p.Id)
		p.RightChop.Unlock()
		p.LeftChop.Unlock()
		fmt.Printf("finishing eating %v\n", p.Id)
	}
	h <- true
}
func scheduler(start chan bool, finished chan bool, wg *sync.WaitGroup) {
	//k := 2
	defer wg.Done()
	start <- true
	start <- true
	for k := 0; k < 4; k++ {
		retValue := <-finished
		if retValue {
			start <- true
		}
	}
}
func main() {
	CSticks := make([]*ChopStick, 5)
	for i := 0; i < 5; i++ {
		CSticks[i] = new(ChopStick)
	}
	philos := make([]*Philo, 5)
	for i := 0; i < 5; i++ {
		first_chop := rand.Intn(5)
		philos[i] = &Philo{
			Id:        i + 1,
			LeftChop:  CSticks[first_chop],
			RightChop: CSticks[(first_chop+1)%5],
		}
	}
	sched := make(chan bool, 2)
	host := make(chan bool, 5)

	var wg sync.WaitGroup
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go philos[i].Eat(sched, host, &wg)
	}
	// host
	wg.Add(1)
	go scheduler(sched, host, &wg)
	wg.Wait()
}
