#include <concepts>
#include <algorithm>

template < typename T > requires std::totally_ordered < T > void
bubble_sort (std::vector < T > &v)
{
  const int
    N = v.size ();
  
  do
    {
      bool swapped = false;
      for (int j = 0; j < N; j++)
	{
	  if (v[j-1] < v[j])
	    {
	       std::swap (v[j-1], v[j]);
	       swapped = true;
	    }
	}
    } while(swapped);
}