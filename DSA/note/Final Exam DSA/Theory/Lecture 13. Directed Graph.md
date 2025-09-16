
# Introduce
- Digraph: directed edges
- path
- shortest path
- topological sort
- strong connectivity
- transitive closure
- page rank

# Digraph API
- Digraph(int V)
- Digraph(In in)
- addEdge
- adj
- V
- E
- reverse
![[Pasted image 20241225105747.png]]

- In practice: use adj-lists representation
	- Algorithms based on iterating over vertices pointing from v
	- Real-world digraphs tent to be sparse (not dense).

# Digraph search
- DFS
	- Reachability application
- BFS
	- Multiple-source shortest paths: enqueuing all source
	- web crawler: take most related website, if use DFS it will crawl further and further


# Topological sort
- Goal. Given a set of tasks to be completed with precedence constraints, in which order should we schedule the tasks?
- Digraph model. vertex = task; edge = precedence constraint.

- DAG: directed acyclic graph
- Topological sort: redraw DAG so all edges point upwards

- Solution: 
	- Run DFS
	- Return vertices in reverse post order
	- ![[Pasted image 20241225111602.png]]

	- Proof: Reverse DFS postorder of a DAG is a topological order:
		- All vertices pointing from v are done before v is done, so they appear after v in topological order

# Strong component

- A strong component is a maximal subset of strongly-connected vertices.
- In a DAG, no two vertices v and w can be in the same strong component—if there were, there would be both a directed path from v to w and one from w to v, which implies that the digraph has a directed cycle. Thus, each vertex is its own strong component $\implies$ DAG has V strong components.