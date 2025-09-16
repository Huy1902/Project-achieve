package Lec2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class UnionFind {
  private int[] parent;
  private int[] size;
  private int count;

  public UnionFind(int n) {
    parent = new int[n];
    size = new int[n];
    count = n;
    for (int i = 0; i < n; i++) {
      parent[i] = i;
      size[i] = 1;
    }
  }

  public int find(int p) {
    while (p != parent[p]) {
      parent[p] = parent[parent[p]];
      p = parent[p];
    }
    return p;
  }

  public void union(int p, int q) {
    int findP = find(p);
    int findQ = find(q);
    if (findP == findQ) {
      return;
    }
    if (size[findP] < size[findQ]) {
      parent[findP] = findQ;
      size[findQ] += size[findP];
    } else {
      parent[findQ] = findP;
      size[findP] += size[findQ];
    }
    --count;
  }

  public int count() {
    return count;
  }

  public static void main(String[] args) {
    int n = StdIn.readInt();
    UnionFind uf = new UnionFind(n);
    while (!StdIn.isEmpty()) {
      int p = StdIn.readInt();
      int q = StdIn.readInt();
      if (uf.find(p) == uf.find(q)) continue;
      uf.union(p, q);
      StdOut.println(p + " " + q);
    }
    StdOut.println(uf.count() + " components");
  }
}
