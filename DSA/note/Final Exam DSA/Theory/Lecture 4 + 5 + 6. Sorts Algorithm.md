# 1. Selection sort
- Find the smallest element in remaining entry => swap
- Insensitive with input, always $N^2$ 
# 2. Insertion sort
+ Swap current with each larger entry to its left
+ Best case: O(N) - ascending order  
+ Worst cast: O(N^2) - descending order  
+ For partially-sorted array: we knew number of swap equal to number of inversion which is <= c * N in this case.  Hence, running time of insertion sorted with partially-sorted array is linear time O(N). (REMEMBER)
+ Linear in partially sort, sorted (it swap when having inversion)

![[Pasted image 20241220230053.png]]

# 3. Merge sort

- Number of compare $\leq$ $N\log(N)$ 
- Number of array access  $\leq$ $6N\log(N)$ 
- Bottom-up merge-sort (non use recursive, slower than top-down on system): number of passes over input array is logarithm (like recursive in top-down)
- Proof optimal when cost model is compare: at least N! different ordering $\implies$ At least $log(N!)$ $\sim$ $N\log(N)$ (lower bound = upper bound $\implies$ optimal)
- Not optimal with memory usage (not in place).
- Make use of cache than heap sort, data from heap sort is scattering.
![[Pasted image 20241220231014.png]]

# 4. Quick sort
- Shuffle
- Partition
- Sort
- Cost model: compare
	- Best case: $\sim$ $N\log(N)$
	- Worst case: $\sim$ $\frac{1}{2}$ $N^2$ 
	- Average case: $\sim$ 2 ln N $\sim$ $1.39$ $N\log(N)$ (more compare than merge sort but faster in practice because of less data movement)
	- Worst, best depend on how we choose partition
	- The best case occurs when we select the pivot as the mean.
	- The worst case will occur when the array gets divided into two parts, one part consisting of N-1 elements and the other and so on
- For choose first element as pivot:
	- Few distinct key:
	- Increasing order:  worst case
	- Decreasing order:
	- Partially sorted:
	- Randomized:
- Space: It is in place because extra array is $\leq$ c * $\log(N)$. But with overhead use it will:
	- **Worst-case scenario:*** **O(n)*** due to unbalanced partitioning leading to a skewed recursion tree requiring a call stack of size O(n).
	- ***Best-case scenario: O(log n)*** as a result of balanced partitioning leading to a balanced recursion tree with a call stack of size O(log n).
- The goal of 3-way partitioning is to speed up quicksort in the presence of duplicate keys. linearithmic to linear.
- Remember: Stop scans on items equal to the partitioning item. (make different in few distinct key case)


![[Pasted image 20241221141346.png]]

# 5. Heap sort


- Heap construction: using bottom-up method - use $\leq$ 2N compares and exchanges
	- Number of exchanges at most (with binary heap is perfect and height is h):
		![[Pasted image 20241222115458.png]]
	- 2 compares per exchange $\implies$ at most 2n compares
- Sort down: Repeatedly delMax from heap
- Heap sort: use $\leq$ 2Nlg N compares and 

![[Pasted image 20241222175428.png]]

