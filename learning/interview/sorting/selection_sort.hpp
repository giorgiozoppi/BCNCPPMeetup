#ifndef SELECTION_SORT_HPP
#define SELECTION_SORT_HPP
#include <algorithm>
#include <functional>
#include <vector>
namespace sorting {
template <typename T>
void selection_sort(std::vector<T> &v, std::function<bool(T, T)> less) {
  const int N = v.size();
  for (int i = 0; i < N; i++) {
    int min = 0;
    for (int j = 0; j < N; j++) {
      if (less(v[j], v[min])) {
        min = j;
      }
      std::swap(v[i], v[min]);
    }
  }
};
} // namespace sorting
#endif