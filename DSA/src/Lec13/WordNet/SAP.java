package Lec13.WordNet;


import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {
  private Digraph digraph;
  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    this.digraph = new Digraph(G);
  }

  private boolean isValidVertex(int vertex) {
    return (vertex >= 0) && (vertex < digraph.V());
  }

  private boolean isValidSetOfVertices(Iterable<Integer> v) {
    for (Integer i : v) {
      if (i == null || !isValidVertex(i)) return false;
    }
    return true;
  }

  private boolean isValidPairOfVertices(Iterable<Integer> v1, Iterable<Integer> v2) {
    return isValidSetOfVertices(v1) && isValidSetOfVertices(v2);
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    if(!isValidVertex(v) || !isValidVertex(w)) {
      throw new IllegalArgumentException();
    }

    int ancestor = ancestor(v, w);

    int pathLength = -1;

    if(ancestor != -1) {
      BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
      BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
      pathLength = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
    }

    return pathLength;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    if(!isValidVertex(v) || !isValidVertex(w)) {
      throw new IllegalArgumentException();
    }

    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
    int shortestAncestor = -1;
    int shortestDistance = Integer.MAX_VALUE;
    ArrayList<Integer> ancestors = new ArrayList<>();

    for(int i = 0; i < this.digraph.V(); i++) {
      if(bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        ancestors.add(i);
      }
    }

    for(Integer ancestor : ancestors) {
      if(bfsV.distTo(ancestor) + bfsW.distTo(ancestor) < shortestDistance) {
        shortestDistance = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
        shortestAncestor = ancestor;
      }
    }
    return shortestAncestor;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if(!isValidPairOfVertices(v, w)) {
      throw new IllegalArgumentException();
    }

    int shortestDistance = -1;

    int ancestor = ancestor(v, w);

    if(ancestor != -1) {
      BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
      BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
      shortestDistance = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
    }

    return shortestDistance;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    if(!isValidPairOfVertices(v, w)) {
      throw new IllegalArgumentException();
    }

    int shortestDistance = Integer.MAX_VALUE;
    ArrayList<Integer> ancestors = new ArrayList<>();
    int shortestAncestor = -1;

    BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
    BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

    for(int i = 0; i < this.digraph.V(); i++) {
      if(bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
        ancestors.add(i);
      }
    }

    for(Integer ancestor : ancestors) {
      if(bfsV.distTo(ancestor) + bfsW.distTo(ancestor) < shortestDistance) {
        shortestDistance = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
        shortestAncestor = ancestor;
      }
    }
    return shortestAncestor;
  }


  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In("src/Lec12/WordNet/digraph.txt");
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length   = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}