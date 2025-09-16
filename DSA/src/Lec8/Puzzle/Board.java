package Lec8.Puzzle;

import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

  private int[] board;
  private int n;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null) {
      throw new IllegalArgumentException();
    }
    if (tiles.length != tiles[0].length) {
      throw new IllegalArgumentException();
    }
    int n = tiles.length;
    board = new int[n * n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        board[i * n + j] = tiles[i][j];
      }
    }
    this.n = n;
  }

  private int[][] oneDimensionToTwoDimension(int[] board) {
    int[][] twoDimension = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        twoDimension[i][j] = board[i * n + j];
      }
    }
    return twoDimension;
  }

  // string representation of this board
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(n + "\n");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        sb.append(String.format("%d ", board[i * n + j]));
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  // board dimension n
  public int dimension() {
    return n;
  }

  // number of tiles out of place
  public int hamming() {
    int count_true = 0;
    for (int i = 0; i < n * n; i++) {
      if (board[i] == i + 1) {
        ++count_true;
      }
    }
    return n * n - 1 - count_true;
  }

  // sum of Manhattan distances between tiles and goal
  private int manhattanDistance(int pos, int goal) {
    int pos_x = pos / n;
    int pos_y = pos % n;
    int goal_x = goal / n;
    int goal_y = goal % n;
    return Math.abs(pos_x - goal_x) + Math.abs(pos_y - goal_y);
  }

  public int manhattan() {
    int count = 0;
    for (int i = 0; i < n * n; i++) {
      if (board[i] != 0 && board[i] != i + 1) {
        count += manhattanDistance(i, board[i] - 1);
      }
    }
    return count;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return hamming() == 0;
  }

  // does this board equal y?
  @Override
  public boolean equals(Object y) {
    if (y == this) {
      return true;
    }
    if (y == null) {
      return false;
    }
    if (!(y instanceof Board)) {
      return false;
    }
    return Arrays.hashCode(this.board) == Arrays.hashCode(((Board) y).board);
  }

  private void swap(int[] board, int i, int j) {
    int temp = board[i];
    board[i] = board[j];
    board[j] = temp;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    List<Board> neighbors = new ArrayList<>();
    int pos_0 = 0;
    for (int i = 0; i < n * n; i++) {
      if (board[i] == 0) {
        pos_0 = i;
        break;
      }
    }
    if (pos_0 - n >= 0) {
//      System.out.printf("pos_0 - n: %d\n", pos_0 - n);
      int[] new_board = board.clone();
      swap(new_board, pos_0, pos_0 - n);
      neighbors.add(new Board(oneDimensionToTwoDimension(new_board)));
    }
    if (pos_0 + n < n * n) {
//      System.out.printf("pos_0 + n: %d\n", pos_0 + n);
      int[] new_board = board.clone();
      swap(new_board, pos_0, pos_0 + n);
      neighbors.add(new Board(oneDimensionToTwoDimension(new_board)));
    }
    if (pos_0 - 1 >= (pos_0 / n) * n) {
//      System.out.printf("pos_0 - 1: %d\n", pos_0 - 1);
      int[] new_board = board.clone();
      swap(new_board, pos_0, pos_0 - 1);
      neighbors.add(new Board(oneDimensionToTwoDimension(new_board)));
    }
    if (pos_0 + 1 < (pos_0 / n + 1) * n) {
//      System.out.printf("pos_0 + 1: %d\n", pos_0 + 1);
      int[] new_board = board.clone();
      swap(new_board, pos_0, pos_0 + 1);
      neighbors.add(new Board(oneDimensionToTwoDimension(new_board)));
    }
    return neighbors;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int pos_0 = 0;
    int pos_1 = 0;
    for(int i = 0; i < n * n; i++) {
      if(board[i] != 0) {
        pos_0 = i;
        break;
      }
    }
    for(int i = 0; i < n * n; i++) {
      if(board[i] != 0 && i != pos_0) {
        pos_1 = i;
        break;
      }
    }
    int[] new_board = board.clone();
    swap(new_board, pos_0, pos_1);
    return new Board(oneDimensionToTwoDimension(new_board));
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    int n = StdIn.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = StdIn.readInt();

    Board board = new Board(tiles);
    System.out.println("Board dimension: " + board.dimension());
    System.out.println("Hamming: " + board.hamming());
    System.out.println("Manhattan: " + board.manhattan());
    System.out.println("Board:\n" + board.toString());
    Iterable<Board> neighbors = board.neighbors();
    for (Board b : neighbors) {
      System.out.println("Neighbor:\n" + b.toString());
    }
    for(int i = 0; i < 10; i++) {
      System.out.println("Twin:\n" + board.twin().toString());
      System.out.println("Is equal: " + board.equals(board.twin()));
    }
  }


}