#include <farmer.hpp>
#include <vector>
#include <chrono>
#include <memory>
#include <iostream>

using std::string;
using std::chrono::time_point;
using std::chrono::high_resolution_clock;
using std::chrono::duration;
using std::vector;
using std::make_unique;

int main() {
  vector<farmer> v{};
  constexpr size_t N(1000);
  time_point<std::chrono::high_resolution_clock> start, end;
  start = high_resolution_clock::now();
  for (size_t i = 0UL; i < N; i++) {
    string name = "farmer_" + i;
    farmer my_farmer{make_unique<basket>(4,10,20), name};
    my_farmer.harvest(100);
    // without using no except we copy because push_back has strong exception guarantee
    v.push_back(std::move(my_farmer));
    
  }
  end = high_resolution_clock::now();
  auto elapsed_time = (end-start);
  std::cout << "Runtime : " << elapsed_time.count() << std::endl; 
  std::cout << "Now we just copy"  << std::endl;
  start = high_resolution_clock::now();
  for (size_t i = 0UL; i < N; i++) {
    string name = "farmer_" + i;
    farmer my_farmer{make_unique<basket>(4,10,20), name};
    my_farmer.harvest(100);
    v.push_back(my_farmer);
    
  }
  end = high_resolution_clock::now();
  elapsed_time = (end-start);
  std::cout << "Runtime : " << elapsed_time.count() << std::endl; 

}
