package LeetCode;

import edu.princeton.cs.algs4.StdOut;

public class NumberOfIsland {
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
    public int numIslands(char[][] grid) {
      int m = grid.length;
      int n = grid[0].length;
      int number_of_islands = 0;
      for (int i = 0; i < m; ++i) {
        for (int j = 0; j < n; ++j) {
          if (grid[i][j] == '1') {
            ++number_of_islands;
          }
        }
      }
//      StdOut.println(number_of_islands);
      UnionFind uf = new UnionFind(m * n);
      int[] movex = {0, 0, -1, 1};
      int[] movey = {-1, 1, 0, 0};
      for (int i = 0; i < m; ++i) {
        for (int j = 0; j < n; ++j)
          if (grid[i][j] == '1') {
            for (int k = 0; k < 4; ++k) {
              int x = i + movex[k];
              int y = j + movey[k];
              if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] == '0') {
                continue;
              }
              if (uf.find(i * n + j) == uf.find(x * n + y)) {
                continue;
              }
              uf.union(i * n + j, x * n + y);
              --number_of_islands;
//              StdOut.println(i + " " + j + " " + x + " " + y + " " + number_of_islands);
            }
          }
      }
      return number_of_islands;
    }
  }

  public static void main(String[] args) {
    char grid[][] = new char[][]{
            {'1', '1', '1', '1', '0'},
            {'1', '1', '0', '1', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '0', '0', '0'}
    };
    Solution sol = new Solution();
    StdOut.println(sol.numIslands(grid));
  }
}

