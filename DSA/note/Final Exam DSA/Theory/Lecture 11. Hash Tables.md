
# 1. Hash function:
- Method for computing array index from key
- Issue hashing:
	- Hash function
	- Equality test
	- Collision resolution
- Space-time tradeoff
- Uniform hashing assumption
- ![[Pasted image 20241223155937.png]]
# 2. Separate chaining
- Use an array of M < N linked list:
	- Hash: map key to integer i between 0 and M - 1
	- Insert: put at from of $i^{th}$ chain
	- Search: need to search only $i^{th}$ chain
	- Typical choice: M ~ N / 5 $\implies$ constant-time ops
		- M too large : too many empty chains
		- M too small: chain too long

# 3. Linear probing
- Hash: Map key
- Search: search table index il if occupied but no match, try i + 1, i + 2, etc
- Insert: put at table index i if free; if not try i + 1, i + 2, etc
- Typical choice: N / M $\sim \frac{1}{2}$
	- Quick explain: Knuth's parking problem: With M / 2 cars, mean displacement is ~ 3 / 2. This is optimize choice so we need park with half free. 
	- In other word, we have M parking spaces and N cars, N / M $\sim \frac{1}{2}$ to probes for search hit is about 3 / 2
- Delete
	- The easiest way to implement delete is to find and remove the key–value pair and then to reinsert all of the key–value pairs in the same cluster that appear after the deleted key–value pair. If the hash table doesn't get too full, the expected number of key–value pairs to reinsert will be a small constant.
	- An alternative is to flag the deleted linear-probing table entry so that it is skipped over during a search but is used for an insertion. If there are too many flagged entries, create a new hash table and rehash all key–value pairs.

![[Pasted image 20241223163616.png]]

![[Pasted image 20241223165931.png]]

# 4. Hash tables vs balanced search trees:
- Hash tables:
	- Simpler
	- No effective alternative for unordered keys.
	- Faster for simple keys (a few arithmetic ops versus log N compares)
	- Better system support in Java for strings
- Balanced search trees:
	- Stronger performance guarantee
	- Support for ordered Symbol table operations
	- Easier to implement compareTo() correctly than equals() and hashCode()
- In java:
	- Red-black BSTs: java.util.TreeMap, java.util.TreeSet
	- Hash tables: java.util.HashMap, java.util,IdentityHashMap