

# Question 1

**Monotonic shortest path.** Given an edge-weighted digraph G, design an E log ⁡E algorithm to find a _monotonic_ shortest path from s to every other vertex. A path is _monotonic_ if the sequence of edge weights along the path are either strictly increasing or strictly decreasing.

_Hint_: relax edges in ascending order to find a best monotonically increasing path; relax edges in descending order to find a best monotonically decreasing path.
- Sort weight of edge and adjust relax condition
# Question 2

**Second shortest path.** Given an edge-weighted digraph and let P be a shortest path from vertex s to vertex t. Design an Elog⁡V algorithm to find a path (not necessarily simple) other than P from ss to t that is as short as possible. Assume all of the edge weights are strictly positive.

_Hint_: compute the shortest path distances from s to every vertex and the shortest path distances from every vertex to t.
- Loop for all vertex to find second shortest path
- Use reverse graph to find shortest path distances from every vertex to t
# Question 3

**Shortest path with one skippable edge.** Given an edge-weighted digraph, design an Elog⁡V algorithm to find a shortest path from s to t where you can change the weight of any one edge to zero. Assume the edge weights are nonnegative.

- Same idea like question 2, after find suitable for s and t
- We use this to try make vertex be zero one by one