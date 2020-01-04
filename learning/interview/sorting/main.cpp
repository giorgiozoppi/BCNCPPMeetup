
#include <algorithm>
#include <numeric>
#include <random>
#include <bubble_sort.hpp>
#include <chrono>
#include <insertion_sort.hpp>
#include <merge_sort.hpp>
/*
#include <selection_sort.hpp>
*/
#include <iostream>
using namespace std;

template <typename T> void print(const string& str, const std::vector<T> &data) {
  std::cout << str << ": ";
  for (const auto &item : data) {
    std::cout << item << " ";
  }
  std::cout << std::endl;
 
}
int main(int argc, char **argv) {
  constexpr int test_size = 10 * 2048;
  std::vector<long> sample(test_size);
  std::iota(sample.begin(), sample.end(), 100);
  std::random_device rd;
  std::mt19937 g(rd());
  std:function<bool(long,long)> cmp = [](long a, long b) { return a > b; };
  std::cout << "Benckmark sorting of " << test_size << " values "<< std::endl;
  std::shuffle(sample.begin(), sample.end(), g);
  //print("[SHUFFLE]", sample);
  auto start = std::chrono::system_clock::now();
  sorting::bubble_sort(sample, cmp);
  auto end = std::chrono::system_clock::now();
  //print("[BUBBLESORT]", sample);
  int elapsed_time = std::chrono::duration_cast<std::chrono::milliseconds>
                             (end-start).count();
  std::cout << "Bubble Sort time a vector of items "<< test_size << ":" << elapsed_time << "ms\n";  
  std::shuffle(sample.begin(), sample.end(), g);
//  print("[SHUFFLE]",sample);
  start = std::chrono::system_clock::now();
  sorting::insertion_sort(sample, cmp);
  end = std::chrono::system_clock::now();
  elapsed_time = std::chrono::duration_cast<std::chrono::milliseconds>
                             (end-start).count();
  std::cout << "Insertion Sort time a vector of items "<< test_size << ":" << elapsed_time << "ms\n";    
 // print("[INSERTIONSORT]", sample);
  std::shuffle(sample.begin(), sample.end(), g);
 // print("[SHUFFLE]",sample);
  start = std::chrono::system_clock::now();
  sorting::merge_sort(sample, cmp);
  end = std::chrono::system_clock::now();
  elapsed_time = std::chrono::duration_cast<std::chrono::milliseconds>
                             (end-start).count();
  std::cout << "MergeSort time a vector of items "<< test_size << ":" << elapsed_time << "s\n";    
  //print("[MERGESORT]", sample);
  return 0;
}
