
# Question 1
**Diameter and center of a tree.** Given a connected graph with no cycles

- _Diameter_: design a linear-time algorithm to find the longest simple path in the graph.
    
- _Center_: design a linear-time algorithm to find a vertex such that its maximum distance from any other vertex is minimized.


_Hint (diameter)_: to compute the diameter, pick a vertex s; run BFS from s; then run BFS again from the vertex that is furthest from s.
- First run to find endpoint of a vertex
- Second bfs to ensure find a longest path

_Hint (center)_: consider vertices on the longest path.
- Choose middle point of longest path

# Question 3

**Euler cycle.** An Euler cycle in a graph is a cycle (not necessarily simple) that uses every edge in the graph exactly one.

- Show that a connected graph has an Euler cycle if and only if every vertex has even degree.
    
- Design a linear-time algorithm to determine whether a graph has an Euler cycle, and if so, find one.

_Hint:_ use depth-first search and piece together the cycles you discover.
- Hierholzer's algorithm
![[Pasted image 20241225013249.png]]
