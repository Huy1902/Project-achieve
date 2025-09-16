# 1. Question 1

Under which of the following scenarios does the $N\log(N)$ lower bound for sorting apply? Assume that the keys are accessed only through the compareTo() method unless otherwise specified.
- No two keys are equal

# 2. Question 2

**Merging with smaller auxiliary array.** Suppose that the subarray a[0] to a[n−1] is sorted and the subarray a[n] to a[2∗n−1] is sorted. How can you merge the two subarrays so that a[0] to a[2∗n−1] is sorted using an auxiliary array of length n (instead of 2n)?
- Copy only the left half into the auxiliary array.

# 3. Question 3

**Counting inversions**. An _inversion_ in an array a[ ] is a pair of entries a[i] and a[j] such that i<j but a[i]>a[j]. Given an array, design a linearithmic algorithm to count the number of inversions.

- _Hint_: count while merge sorting.

# 4. Question 4
**Shuffling a linked list.** Given a singly-linked list containing N items, rearrange the items uniformly at random. Your algorithm should consume a logarithmic (or constant) amount of extra memory and run in time proportional to $N\log(N)$ in the worst case.

- _Hint_: design a linear-time subroutine that can take two uniformly shuffled linked lists of sizes $n_1$ and $n_2$​ and combined them into a uniformly shuffled linked lists of size $n_1+n_2$.
- Divide and Conquer approach
- When merge, generate a random variable from $[0, 1)$ if r < $\frac{n_1}{n_1 + n_2}$, take the next node from $L_1$ otherwise take the next node from $L_2$.
- Find middle use slow and faster technique(one jump one step and other jump two) to find middle of linked-list to divide. $\theta(N)$  (more precise $\sim$ N / 2)
- It will bonus $\theta(N)$ to merge that two halves (conquer by random choose from two halves)
- It still ensures linearithmic.

# 5. Question 5

**Nuts and bolts.** A disorganized carpenter has a mixed pile of n nuts and n bolts. The goal is to find the corresponding pairs of nuts and bolts. Each nut fits exactly one bolt and each bolt fits exactly one nut. By fitting a nut and a bolt together, the carpenter can see which one is bigger (but the carpenter cannot compare two nuts or two bolts directly). Design an algorithm for the problem that uses at most proportional to $n\log⁡(n)$ compares (probabilistically).

```
pivot_nut = nuts[lo]
pivot_index_bolt = partition(bolts, lo, hi, pivot_nut) 
pivot_bolt = bolts[pivot_index_bolt] 
partition(nuts, lo, hi, pivot_bolt) 
match_nuts_and_bolts(nuts, bolts, lo, pivot_index_bolt - 1) match_nuts_and_bolts(nuts, bolts, pivot_index_bolt + 1, hi)
```

- Use nut as pivot then use that to partition bolts
- When partition bolts we will know which bolt is fit with nut then use that fitted bolts to partition bolts
# 6. Question 6
**Selection in two sorted arrays.** Given two sorted arrays a[ ] and b[ ], of lengths $n_1$​ and $n_2$​ and an integer $0 ≤ k < n_1+n_2$​, design an algorithm to find a key of rank k. The order of growth of the worst case running time of your algorithm should be $log(⁡n)$, where $n=n_1+n_2$.

- Approach 1: Adjust binary search on both two array
	- Use median $m_1$ $m_2$ of a and b
	- If k < $m_1 + m_2$ discard the larger half of the array with larger median
	- Otherwise, discard the smaller half of the array with smaller median
	- Adjust k accordingly after discarding elements.
	- Until k == 0 (in that case we eliminate all except two remaining elements) now, we choose minimum between them.
- Approach 2: Binary search on one array
	- Binary search on array a, ensure a is smaller size array (will be binary search for $O(min(n_1, n_2))$ )
	- With pivot i for a, we find j = k - i and check $b_{j - 1} \leq a_{i - 1} \leq b_{j} \implies a_{i - 1} ,\text{otherwise } a_{i - 1} \leq b_{j - 1} \leq a_i \implies b_{j - 1}$ 
	- If $a_{i - 1} > b_{j + 1}$, hi = i, otherwise lo = i + 1

# 7. Question 7
**Decimal dominants.** Given an array with n keys, design an algorithm to find all values that occur more than n/10 times. The expected running time of your algorithm should be linear.

- Use 9 counter
- First loop, find 9 candidates, if already have 9 , decrease all 9 candidate 1 if a candidate have value become 0, delete it and replace by new candidate
- After first loop, we have 9 candidate can have frequencies > n / 10
- Second loop count actual frequencies of them
- First loop c * N and second is N so we can ensure it is linear