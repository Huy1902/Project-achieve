# Stack

## 1. API
- push
- pop
- isEmpty
## 2. Implementation
- Linked-list
- Resizing array
	- Twice when full: amortized cost of adding: $\sim$ 3N at worst case
	- Halve when one-quarter full
	- ![[Pasted image 20241220185944.png]]
- Compare:
	- Linked-list: 
		- Constant time in worst case
		- Uses extra time and space to deal with the links
	- Resizing array:
		- Every operation takes constant amortized time
		- Less wasted space

# Queue

## 1. API
- Enqueue
- Dequeue
- isEmpty()

## 2. Implementation
- Linked-list: last node (remember)
- Resizing array
	- Twice when full: amortized cost of adding. Remember when resize:
		$$
		copy[i] = q[(first + i) \% q.length];
		$$
	- Halve when one-quarter full
- Complexity is similar to Stack

