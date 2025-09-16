package LeetCode;

import edu.princeton.cs.algs4.StdOut;

public class Couples_Holding_Hands {
  static class UnionFind {
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
  }

  static class Solution {
    public int minSwapsCouples(int[] row) {
      // Imagine we have row.length / 2 two-seat couches, and we need to make two people on
      // a couch is a couple. We need to swap when one of them on different couch with another one
      // We draw a connection from a person in couple to another one
      // Hence, we can get number of minimum swap by subtraction number of couches minus number of
      // connected set (union set) ( is represented for how many step we need to break a set of couches
      // into single couches
      UnionFind couches = new UnionFind(row.length);
      for(int i = 0; i < row.length; i += 2) {
        couches.union(row[i], row[i + 1]);
      }
      for(int i = 0; i < row.length; ++i) {
        if(couches.find(row[i] / 2 * 2) != couches.find(row[i] / 2 * 2 + 1)) {
          couches.union(row[i] / 2 * 2, row[i] / 2 * 2 + 1);
        }
      }
//      StdOut.println(couches.count());
      return row.length / 2 - couches.count();
    }
  }

  public static void main(String[] args) {
    int[] row = new int[]{0, 2, 1, 3};
    Solution sol = new Solution();
    StdOut.println(sol.minSwapsCouples(row));
  }
}

