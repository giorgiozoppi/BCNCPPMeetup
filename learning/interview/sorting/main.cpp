#include <bubble_sort.hpp>
#include <insertion_sort.hpp>
#include <merge_sort.hpp>
#include <selection_sort.hpp>

template <typename T> print(const T& data[])
{
  for (const auto& item : data)
  {
    std::cout << item << " ";
  }
}
int main(int argc, char** argv)
{
  int v1[] = { 10, 20, 5, 4, 8 };
  int v2[] = { 30, 4, 10, 2, 1 };
  int v3[] = { 20, 1, 7, 2, 90 };
  int v4[] = { 100, 2, 40, 2, 1 };
  sorting::bubble_sort(v1);
  sorting::selection_sort(v2);
  sorting::insertion_sort(v3);
  sorting::merge_sort(v4);
  print(v1);
  print(v2);
  print(v3);
  print(v4);
  
}
