#include <concepts>
#include <algorithm>

template < typename T > requires std::totally_ordered < T > void
insertion_sort (std::vector < T > &v)
{
  const int
    N = v.size ();
  for (int j = 1; j < N; j++)
    {
      for (int i = j; i > 0 && (v[i] < v[i - 1]); i--)
	{
	  std::swap (v[i], v[i - 1]);

	}
    }
}
