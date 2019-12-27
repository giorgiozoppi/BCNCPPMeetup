#include <concepts>
#include <algorithm>
#include <iterator>
#include <vector>

template < typename T > requires std::totally_ordered < T > void
recursive_sort (std::vector < T > &input, std::vector < T > &aux_buffer,
		int lower_bound, int higher_bound)
{
  if (higher_bound <= lower_bound)
    return;
  int
    middle_pivot = lower_bound + (higher_bound - lower_bound) / 2;
  recursive_sort (input, aux_buffer, lower_bound, middle_pivot);
  recursive_sort (input, aux_buffer, middle_pivot + 1, higher_bound);
  merge (input, aux_buffer, lower_bound, middle_pivot, higher_bound);
}

template < typename T > requires std::totally_ordered < T > void
merge (std::vector < T > &input, const std::vector < T > &aux, int lower,
       int middle, int higher)
{
  int
    i = lower;
  int
    j = middle + 1;
  for (int pos = lower; pos <= higher; pos++)
    {
      aux[pos] = input[pos];
    }
  for (int k = lower; k <= higher; k++)
    {
      if (i > middle)
	{
	  a[k] = aux[j++];
	}
      else if (j > higher)
	{
	  a[k] = aux[i++];
	}
      else if (aux[j] < aux[i])
	{
	  a[k] = aux[j++];
	}
      else
	{
	  a[k] = aux[i++];
	}
    }
}

template < typename T > requires std::totally_ordered < T > void
merge_sort (std::vector < T > &v)
{
  std::vector < T > aux;
  std::copy (v.begin (), v.end (), std::back_inserter (aux));
  const int
    N = v.size ();
  recursive_sort (v, aux, 0, v.size () - 1);
}
