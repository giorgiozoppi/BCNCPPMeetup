#ifndef QUICK_SORT_HPP
#define QUICK_SORT_HPP
#include <algorithm>
#include <functional>
#include <numeric>
#include <random>
#include <iostream>
namespace sorting
{


  template < typename T >
    int q_sort_partition (std::vector < T > &v, const int &lower,
			  const int &upper, std::function < bool (T, T) > cmp)
  {
    auto pivot = v[lower];
    auto i = lower;
    auto j = upper+1;
    while (true) {
      while(cmp(v[++i],pivot)) if (i == upper) break;
      while(cmp(pivot,v[--j])) if (j == lower) break;
      if (i>=j) break;
      std::swap (v[i], v[j]);      
    }
    std::swap (v[pivot], v[j]);      
    return j;
  }
  template < typename T > void q_sort (std::vector < T > &v, const int &lower,
				       const int &upper,
				       std::function < bool (T, T) > cmp)
  {
    if (lower <=upper)
      {
        int pivot = q_sort_partition < T > (v, lower, upper, cmp);
	      q_sort (v, lower, pivot - 1, cmp);
        q_sort (v, pivot + 1, upper, cmp);
      }
  }
  template < typename T > void quick_sort (std::vector < T > &v,
					   std::function < bool (T, T) > cmp,
					   std::default_random_engine &
					   engine)
  {
    int N = v.size ();
    // eliminate the input dependncy
    std::random_shuffle (v.begin (), v.end ());
    q_sort (v, 0, N - 1, cmp);  
  }
}
#endif
