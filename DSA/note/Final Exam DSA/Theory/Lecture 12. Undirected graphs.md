
# 1. Introduce
- Graph
- Path: sequence of vertices connected by edges
- Cycle: Path whose first and last vertices are same
- Two vertices are connected if there is a path between them
- Euler tour: A cycle that uses each edge exactly one
- Hamilton tour: A cycle that uses each vertex exactly one

# 2. API
- Graph(int V)
- Graph(In in)
- addEdge
- adj(int v): vertices adjacent to V
- int V(): number of vertices
- int E(): number of edges
- degree

***Graph representation***
- Set-of-edges: a list of the edges
- Adjacency-matrix: a two-dimensional V-by-V boolean array for each edge v-w
	- Good if graph is dense
	- ***Disallows parallel edges***
- Adjacency-list: vertex-indexed array of lists
	- Good if graph is sparse (usually happen in practice)
![[Pasted image 20241223220656.png]]
- In practice: use adj-lists representation:
	- Algorithms based on iterating over vertices adj to v.
	- Real-world graphs tend to be sparse.

# 3. Depth-First Search DFS:
- In a graph G represented using the adjacency-lists representation, depth-first search marks all vertices connected to s in time proportional to the sum of the degrees of the vertices in the connected component containing s.
	- Depth-first search will examine each vertex in the connected component containing s. It processes edge in this component twice (once in either direction).
- DFS (to visit a vertex v)
	- Mark v as visited
	- Recursively visit all unmarked vertices w adjacent to v
- marked[], edgeTo[v]
- pathTo(): thank to edgeTo[], we can find a path to s (if one exists) in time proportional to its length after DFS
- With depth-first search it is either an explicit stack (with a nonrecursive version) or the function-call stack (with a recursive version).
- An Path API:
	- hasPathTo
	- pathTo: Iterable
# 4. Breadth-First Search BFS:
- The critical data structures used in depth-first search and breadth-first search are stack and queue, respectively.
- Repeat until queue is empty:
	- Remove vertex v from queue
	- Add to queue all unmarked vertices adj to v and mark them
- edgeTo[], disTo[]
- Shortest path
- BFS computes shortest paths (fewest number of edges) from s to all over vertices in a graph in time proportional to E + V.
	- Queue always consists of zero or more vertices of distance k from s, followed by zero or more vertices of distance k + 1


# 5. Connected components:
- The is-connected-to relation is an equivalence relation: it is reflexive, symmetric, and transitive.
	- connected(int v, int w)
	- count
	- id(int v)
- Adjust dfs for that

# 6. Challenges
- Bipartile: DFS
- Cycle: DFS
- Eulerian tour: Fleury algorithm (Don't burn bridge): Only have Eulerian circuit if all vertices have even degree
	1.  Make sure the graph has either 0 or 2 odd vertices. (condition have path not cycle)
	2. If there are 0 odd vertices, start anywhere. If there are 2 odd vertices, start at one of them.
	3. Follow edges one at a time. If you have a choice between a bridge and a non-bridge, _always choose the non-bridge_.
	4. Stop when you run out of edges.
	- Run DFS follow adj-list twice(once to find cycle, and once each to find bridge) $O((V + E)^2)$
- Hamiltonian cycle: NP-complete problem: use DFS to find in V! complexity
- Lay out a graph in the plane without crossing edges?
	- 