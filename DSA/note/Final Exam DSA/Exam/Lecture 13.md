
# Question 1
How many different digraphs are there on VV vertices? Allow self-loops but do not allow parallel edges.

- $2^{V^2}$ 
- There are $V^2$ possible edges. Each edge is either in the digraph or not.


# Question 2
Suppose that a digraph G is represented using the adjacency-lists representation. What is the order of growth of the running time to find all vertices that point to a given vertex v?

- V + E
- You must scan through each of the V adjacency lists and each of the E edges. If this were a common operation in digraph-processing problems, you could associate two adjacency lists with each vertex—one containing all of the vertices pointing from v (as usual) and one containing all of the vertices pointing to v.


# Question 3
Shortest directed cycle:
Run bfs from each vertex

# Question 4
**Hamiltonian path in a DAG**

Hamiltonian path is a simple path that visits every vertex exactly one time
Use topological sort then check each pair in topological order have a directed edge
