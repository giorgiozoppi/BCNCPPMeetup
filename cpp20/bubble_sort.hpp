/**
 * Copyright (c) 2020 Giorgio Zoppi <giorgio.zoppi@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 *all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
#ifndef BUBBLE_SORT_HPP
#define BUBBLE_SORT_HPP
#include <algorithm>
#include <functional>
namespace sorting {
template <typename T>
void bubble_sort(std::vector<T> &v, std::function<bool(T, T)> cmp) {
  const int N = v.size();
  auto swapped = false;
  do {
    swapped = false;
    for (int j = 0; j < N - 1; j++) {
      if (cmp(v[j], v[j+1])) {
        std::swap(v[j], v[j+1]);
        swapped = true;
      }
    }
  } while (swapped);
};
} // namespace sorting
#endif
