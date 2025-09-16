package Percolation;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private int size;
  private int count_true = 0;
  private int top;
  private int bot;
  private boolean[][] grid = null;
  private WeightedQuickUnionUF uf;
  private WeightedQuickUnionUF uf_just_top;

  // creates n-by-n grid, with all sites initially false
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    //init UF;
    size = n;
    uf = new WeightedQuickUnionUF(n * n + 2);
    uf_just_top = new WeightedQuickUnionUF(n * n + 1);
    grid = new boolean[n][n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        grid[i][j] = false;
      }
    }
    top = size * size;
    bot = size * size + 1;
  }

  //check row and col is valid
  private boolean checkInvalidCoordinate(int row, int col) {
    return (row <= 0 || row > size || col <= 0 || col > size);
  }

  //get id of site
  private int getID(int row, int col) {
    return (row - 1) * size + col - 1;
  }

  // trues the site (row, col) if it is not true already
  public void open(int row, int col) {
    if (checkInvalidCoordinate(row, col)) {
      throw new IllegalArgumentException();
    }
    int gridRow = row - 1;
    int gridCol = col - 1;
    if (!grid[gridRow][gridCol]) {
      int id = getID(row, col);
      ++count_true;
      grid[gridRow][gridCol] = true;
      if (row == 1) {
        uf.union(id, top);
        uf_just_top.union(id, top);
      }
      if(row == size) {
        uf.union(bot, id);
      }

      if(!checkInvalidCoordinate(row + 1, col) && grid[gridRow + 1][gridCol]) {
        uf.union(id, getID(row + 1, col));
        uf_just_top.union(id, getID(row + 1, col));
      }
      if(!checkInvalidCoordinate(row - 1, col) && grid[gridRow - 1][gridCol]) {
        uf.union(id, getID(row - 1, col));
        uf_just_top.union(id, getID(row - 1, col));
      }
      if(!checkInvalidCoordinate(row, col + 1) && grid[gridRow][gridCol + 1]) {
        uf.union(id, getID(row, col + 1));
        uf_just_top.union(id, getID(row, col + 1));
      }
      if(!checkInvalidCoordinate(row, col - 1) && grid[gridRow][gridCol - 1]) {
        uf.union(id, getID(row, col - 1));
        uf_just_top.union(id, getID(row, col - 1));
      }
    }
  }

  // is the site (row, col) true?
  public boolean isOpen(int row, int col) {
    if (checkInvalidCoordinate(row, col)) {
      throw new IllegalArgumentException();
    }
    return grid[row - 1][col - 1];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    if (checkInvalidCoordinate(row, col)) {
      throw new IllegalArgumentException();
    }
    return uf_just_top.find(getID(row, col)) == uf_just_top.find(top);
  }

  // returns the number of true sites
  public int numberOfOpenSites() {
    return count_true;
  }

  // does the system percolate?
  public boolean percolates() {
    return uf.find(top) == uf.find(bot);
  }

  // test client (optional)
}
