#ifndef QUICK_SORT_HPP
#define QUICK_SORT_HPP
#include <algorithm>
#include <functional>
#include <numeric>
#include <random>
namespace sorting
{
template <typename T> void quick_sort(std::vector<T>&v, int lower, int upper, 
                            std::function<bool(T,T)> cmp,  const 
                            std::default_random_engine& engine)
{
    if (lower < upper)
    {
        int pivot = partition(v, lower, upper , cmp);
        quick_sort(v, lower, pivot-1, cmp, engine);
        quick_sort(v, pivot+1, upper,cmp, engine);
    }
}
template <typename T> void quick_sort(std::vector<T>& v, std::function<bool(T,T)> cmp)
{
    std::random_device rd;
    std::default_random_engine random_engine(rd());
    std::mt19937 g(rd());
    int N = v.size();
    // eliminate the input dependncy
    std::shuffle(v.begin(), v.end(), g);
    quick_sort(v, 0, N-1, cmp, random_engine);
}
template <typename T> int partition(std::vector<T>& v, 
    const int& lower, 
    const int& upper, 
    std::function<bool(T,T)> cmp)
{
    int i = 0;
    std::uniform_int_distribution<int> uniform_dist(lower, upper);
    int pivot = uniform_dist(engine);
    auto partition_value = v[pivot];
    auto i = lower - 1;
 for (auto j = lower; j < upper -1; j++)
 {
     if (v[j]<=partition_value)
     {
         i++;
         std::swap(v[i],v[j]);
     }
 }
 std::swap(v[i+1], v[partition_value]);
 return i;
}
}
#endif