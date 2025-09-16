  
# Question 1

**Dynamic median.** Design a data type that supports _insert_ in logarithmic time, _find-the-median_ in constant time, and _remove-the-median_ in logarithmic time. If the number of keys in the data type is even, find/remove the _lower median_.

- _Hint_: maintain _two_ binary heaps, one that is max-oriented and one that is min-oriented.
# Question 2

**Randomized priority queue.** Describe how to add the methods sample() and delRandom() to our binary heap implementation. The two methods return a key that is chosen uniformly at random among the remaining keys, with the latter method also removing that key. The sample() method should take constant time; the delRandom() method should take logarithmic time. Do not worry about resizing the underlying array.

- _Hint_: use sink() and swim().

# Question 3

**Taxicab numbers.** A _taxicab_ number is an integer that can be expressed as the sum of two cubes of positive integers in two different ways: $a^3+b^3=c^3+d^3$. For example, 1729 is the smallest taxicab number: $9^3+10^3=1^3+12^3$. Design an algorithm to find all taxicab numbers with a, b, c, and dd less than n.

- Version 1: Use time proportional to $n^2log(⁡n)$ and space proportional to $n^2$.
    
- Version 2: Use time proportional to $n^2log(⁡n)$ and space proportional to n


_Hints:_

- Version 1: Form the sums $a^3+b^3$ and sort.
	- Save all the sums need space proportional to $n^2$ ($n^2$)
	- Sort and check duplicate ($n^2log(n)$)
- Version 2: Use a min-oriented priority queue with n items.
	- Initial a heap with 
	- $$ for\ a\ in\ range(1, n): heapq.heappush(heap, (a**3 + a**3, a, a)) $$
	- Pop min number from heap then check for duplicate with previous sum, if is the same add it to equal sum array.
	- When meet different sum and length of equal sum array > 1, we consider it as a part of result
	- Then, we increasing b a unit (b++), initially we create b as a now we increasing it to find all remaining case (b + 1 <= n for sure)