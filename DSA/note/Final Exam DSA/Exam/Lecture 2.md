
# 1. Ex1

**Search in a bitonic array.** An array is _bitonic_ if it is comprised of an increasing sequence of integers followed immediately by a decreasing sequence of integers. Write a program that, given a bitonic array of nn distinct integer values, determines whether a given integer is in the array.

- Standard version: Use ∼3lg⁡n compares in the worst case.
    
- Signing bonus: Use ∼2lg⁡n compares in the worst case (and prove that no algorithm can guarantee to perform fewer than ∼2lg⁡n compares in the worst case).

_Hints_: Standard version. First, find the maximum integer using ∼1lg⁡n compares—this divides the array into the increasing and decreasing pieces.

Step 1: ~lgn
Compare the middle element with its neighbors 
If the middle element is greater than both, it is peak
Step 2: $\sim$ $\log$ n
Binary search for increasing sequence
Step 3: ~lgn
Binary search for descending sequence

Signing bonus. Do it without finding the maximum integer.

Pf lower bound: Any algorithm must decide between the two halves (increasing vs. decreasing) at least once.
Binary search adjusted with determining direction based on neighbors:
- Decreasing: trivial
- Increasing: trivial
	- At peak: recursive left or right

# 2. Ex2
  
Question 3

**Egg drop.** Suppose that you have an n-story building (with floors 1 through n) and plenty of eggs. An egg breaks if it is dropped from floor T or higher and does not break otherwise. Your goal is to devise a strategy to determine the value of T given the following limitations on the number of eggs and tosses:

- Version 0: 1 egg, ≤T tosses.
    
- Version 1: ∼1lgn eggs and ∼1lgn tosses.
    
- Version 2: ∼lg⁡T eggs and ∼2lgT tosses.
    
- Version 3: 2 eggs and ∼2n​ tosses.
    
- Version 4: 2 eggs and ≤cT​ tosses for some fixed constant cT.


- Version 0: sequential search.
	- Try from 1 until egg break, it's T
- Version 1: binary search.
	- Use binary search start from middle floor
- Version 2: find an interval containing T of size ≤2T, then do binary search.
	- Exponential binary search, 1, 2, 4, 8, ... 2T (1 egg and lgT)
	- When egg break for example it's 16 floor 
	- Binary search from 9 $\rightarrow$ 15 floor (plus 1 egg and lgT)
- Version 3: find an interval of size n​, then do sequential search. Note: can be improved to ∼2n​ tosses.
	- Interval $\sqrt(N)$ search for $1\sqrt(N)$ $2\sqrt(N)$ $3\sqrt(N)$ and so on
	- If egg break then do sequential search
- Version 4: 1+2+3+…+t  ∼  $\frac{1}{2}$ $t^2$ . Aim for c = $2\sqrt(2)$ 
	- Choose x then we drop at x, x + (x - 1), x + (x - 1) + (x - 2), and so on
	- Like that we will have a sequence x + (x - 1) + ... + 1 which $\geq$ 2T
	- x $\approx$ $\sqrt(2T)$ 
	- If egg break at floor f then sequential search from f - (x - k) to f - 1