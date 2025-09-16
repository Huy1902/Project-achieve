
# 1. API:
- put: overwrite old value with new value
- get:  return null if key not present
- delete: remove key from table
- contains
- isEmpty()
- size()
- keys()
![[Pasted image 20241222221952.png]]

# 2. Binary Search Tree
## a. API (elementary + ordered operation):
- We use size for rank so remember update count in insert or delete
- Search: get()
- Insert: put()
- height: if root == null: height is -1, use max of height left and right to find height of tree
- size: number of key-value pairs
- delMin
- delMax
- delete
	- tombstone: trivial
	- Hibbard deletion:
		- Search for key
		- no right child
		- no left child
		- replace with successor (maybe min or max it depend on use delMin or delMax)
		- update subtree counts
	- Then it unbalance tree
![[Pasted image 20241222232944.png]]
- min
- max
- floor: largest key $\leq$ a given key: 
	- Case 1: equal -> done
	- Case 2: key less than root -> go to left
	- Case 3: key greater than root -> root or maybe is in right side (if it exists)
- ceiling: smallest key $\geq$ a given key: similar with floor
	- Case 1: equal -> done
	- Case 2: key greater than root -> go to right
	- Case 3: key less than root -> root or maybe it is in left sight?
- rank: how many keys < k?: use size of node
	- Case 1: key less than root? find rank in left side
	- Case 2: key equal root? rank is size of left node
	- Case 3: key greater than root? rank = size of left node + 1 (itself) + rank of key in right side
- select: key of rank k. ***REMEMBER*** : rank k mean have k keys < key with rank k.
- inorder: inorder traversal: left -> enqueue key -> right: yields keys in ascending order. With no duplicate keys, it becomes similar to quick sort
![[Pasted image 20241222231548.png]]

## b. Mathematical analysis:
- BSTs and quicksort partitioning: Correspondence is 1-1 if array has no duplicate keys. The, the expected number of compares for a search/insert is ~ 2 ln N ~ 1.39 N log N
- If N distinct keys are inserted in random order, expected height of tree is 4.311 ln N
- Worst case heigh is N

# 3. 2 - 3 Tree:

- The height of a 2–3 tree increases only when the root node splits, and this happens only when every node on the search path from the root to the leaf where the new key should be inserted is a 3-node.
- 2 - node: one keys
- 3 - node: two keys
- Symmetric order
- Perfect balance
- Insertion:
	- Temporary 4 - node
	- Move middle key
	- Repeat
	- Split
- Maintain symmetric + perfect balance:
	- Proof: each transformation:
		- root
		- 2 - node
			- left
			- right
		- 3 - node
			- left
			- middle
			- right
- Worst case: lg N [all 2 nodes]
- Best case: $log_3(N)$ $\approx$ .631 lg N [all 3 nodes]
- $\implies$ Guaranteed logarithm performance
![[Pasted image 20241223134729.png]]

# 4. Red - Black Tree
Leaf-leaning red-black BSTs
- Modify the structure of the BST to encode the color information.
- Red links lean left
- Same number of black links from root to null
- No node has two red links connected to it
- Elementary operation:
	- Left rotation
	- Right rotation
	- Color flip
- The height of any red–black BST on n keys (regardless of the order of insertion) is guaranteed to be between lg n and 2 lg n.
	- Pf: every path from root to null has same number of black links
		- never two red links in-a-row
- Insertion:![[Pasted image 20241223140229.png]]
- ![[Pasted image 20241223140347.png]]
- AVL is better for insert and delete
- Red-black is better for search