
# APIs
- DirectedEdge:
	- from
	- to
	- weight
- EdgeWeightedDigraph
	- addEdge
	- adj
	- V()
	- E()
	- edges()
- SP
	- distTo
	- pathTo

# Shortest Paths
- distTo[v] is length of shortest path from s to v
- edgeTo[v] is last edge on shortest path from s to v
- Edge relaxation:
- Shortest-paths optimality conditions
- Generic shortest-paths: edge relaxation for distTo, repeat until optimality conditions are satisfied
- How to choose edge to relax?
	- Dijkstra


# Dijkstra's algorithm
- Consider vertices in increasing order of distance from s.
- Add vertex to tree and relax all edges pointing from that vertex.
![[Pasted image 20241226004900.png]]