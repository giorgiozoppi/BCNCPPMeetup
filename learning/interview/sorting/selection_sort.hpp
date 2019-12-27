#include <concepts>
#include <algorithm>
namespace sorting
{
template < typename T > requires std::totally_ordered < T > void
selection_sort (std::vector < T > &v)
{
  const int
    N = v.size ();
  for (int i = 0; i < N; i++)
    {
      int
	min = 0;
      for (int j = 0; j < N; j++)
	{
	  if (v[j] < v[min])
	    {
	      min = j;
	    }
	  std::swap (v[i], v[min]);
	}
    }
};
}
