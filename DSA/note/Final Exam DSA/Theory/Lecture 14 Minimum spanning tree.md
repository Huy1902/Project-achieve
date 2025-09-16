
# Introduction

- A spanning tree of G is a subgraph T that is both a tree (connected and acyclic) and spanning (includes all of the vertices)


# Greedy algorithm
- A cut 
- A crossing edge
- Cut property: Given any cut, the crossing edge of min weight is in the MST
	- Pf: contradiction: if min weight e is not in MST then a edge f in a spanning tree. So we can not add e because of creating cyclic and replace f by e will still be a spanning tree with lower weight.
- Greedy MST algorithm:
	- Pf:
	- Cut property: any edge colored black is in the MST
	- Cut with no black crossing edges: fewer than V - 1 black edges (consider cut whose vertices are one connected component $\implies$ no black crossing edges is like connect components into bigger component).
- Choose cut? Find min-weight edge?
	- Kruskal
	- Prim


# Edge - Weighted Graph API:
- Edge(int v, int w, double weight)
- either()
- other(int v)
- compareTo
- There is one Edge object for each edge in the graph. The adjacency lists contain two references to each Edge but there is still only one Edge object per graph edge.

# Kruskal's Algorithm

- Consider edges in ascending order of weight
- Add next edge to tree T unless doing so would create a cycle
- Pf
	- Colors the edge e = v-w black
	- Cut = set of vertices connected to v in tree T
	- No crossing edge is black (Consider cut whose vertices are one connected component. And not choose edge create cycle so it isn't in that component)
	- No crossing edge has lower weight. Because choose minimum edge
- Cycle detection?
	- DFS: $O(V)$
	- union-find (Weighted Quick union with Path compression): $\log^*(V)$ (number of time applied log to have result $\leq$ 1)
- Get minimum edge?
	- Priority Queue
- ![[Pasted image 20241225225155.png]]

# Prim's algorithm
- Start with vertex 0 and greedily grow tree T
- Add to T the min weight edge with exactly one endpoint in T
- Repeat until V - 1 edges
- Pf: a special case of the greedy MST algorithm
	- e = min weight edge connecting a vertex on the tree to a vertex not on the tree
	- Cut = set of vertices connected on tree
	- No crossing edge is black
	- No crossing edge has lower weight
- Find min the weight edge with exactly one endpoint in T
	- Use a priority queue: log E
- Lazy Prim compute the MST in the time proportional to E log E and extra space proportional to E![[Pasted image 20241225231835.png]]
- Eager solution
	- Decrease priority of x if v-x becomes shortest edge connecting x to T
	- For each non-tree vertex v, the eager version of Prim's algorithm maintains at most one entry in the priority queue (with key equal to the weight of the cheapest edge from v to the tree)
	- Complexity E log V in worst case
