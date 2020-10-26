#ifndef MERGE_SORT_HPP
#define MERGE_SORT_HPP
#include <algorithm>
#include <functional>
#include <iterator>
#include <vector>
namespace sorting {
template <typename T>
void rmerge_sort(std::vector<T> &input, std::vector<T> &aux_buffer,
                 int lower_bound, int higher_bound,
                 std::function<bool(T, T)> cmp) {

  if (higher_bound <= lower_bound)
    return;
  int middle_pivot = lower_bound + (higher_bound - lower_bound) / 2;
  rmerge_sort(input, aux_buffer, lower_bound, middle_pivot, cmp);
  rmerge_sort(input, aux_buffer, middle_pivot + 1, higher_bound, cmp);
  merge<T>(input, aux_buffer, lower_bound, middle_pivot, higher_bound, cmp);
};

template <typename T>
void merge(std::vector<T> &input, std::vector<T> &aux, int lower, int middle,
           int higher, std::function<bool(T, T)> cmp) {

  int i = lower;
  int j = middle + 1;
  // should be copied [lower, higher] but by default std:copy [lower,higher)
  std::copy(input.begin()+lower, input.begin()+higher+1, aux.begin()+lower);
  for (int k = lower; k <= higher; k++) {
    if (i > middle) {
      input[k] = aux[j];
      j++;
    } else if (j > higher) {
      input[k] = aux[i];
      i++;
    } else if (cmp(aux[i], aux[j])) {
      input[k] = aux[j];
      j++;
    } else {
      input[k] = aux[i];
      i++;
    }
  };
}
template <typename T>
void merge_sort(std::vector<T> &v, std::function<bool(T, T)> cmp) {
  std::vector<T> aux(v.size());
  std::copy(v.begin(), v.end(), std::back_inserter(aux));
  const int N = v.size();
  rmerge_sort(v, aux, 0, N - 1, cmp);
};
} // namespace sorting
#endif
