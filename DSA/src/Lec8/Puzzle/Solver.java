package Lec8.Puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
  private State lastState = null;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }

    MinPQ<State> minPQ = new MinPQ<>();
    MinPQ<State> twinPQ = new MinPQ<>();
    minPQ.insert(new State(initial, 0, null));
    twinPQ.insert(new State(initial.twin(), 0, null));

    while (true) {
      State curState = minPQ.delMin();
      if (curState.getBoard().isGoal()) {
        lastState = curState;
//          System.out.println("Found solution!");
        return;
      }
      for (Board neighbor : curState.getBoard().neighbors()) {
        if(curState.getPreState() != null && curState.getPreState().getBoard().equals(neighbor)) {
          continue;
        }
        minPQ.insert(new State(neighbor, curState.getMoves() + 1, curState));
      }

      curState = twinPQ.delMin();
      if (curState.getBoard().isGoal()) {
//          System.out.println("Twin found solution!");
        return;
      }
      for (Board neighbor : curState.getBoard().neighbors()) {
        if(curState.getPreState() != null && curState.getPreState().getBoard().equals(neighbor)) {
          continue;
        }
        twinPQ.insert(new State(neighbor, curState.getMoves() + 1, curState));
      }
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return lastState != null;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (lastState == null) {
      return -1;
    }
    return lastState.getMoves();
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    Stack<Board> path = new Stack<>();
    State lastState = this.lastState;
    while (lastState != null) {
      path.push(lastState.getBoard());
      lastState = lastState.getPreState();
    }
    if(path.isEmpty()) {
      return null;
    }
    return path;
  }

  private class State implements Comparable<State> {
    private Board board;
    private int moves;
    private int cost;
    private State preState = null;

    private State(Board board, int moves, State preState) {

      this.board = board;
      this.moves = moves;
      cost = moves + board.manhattan();
      this.preState = preState;
    }

    @Override
    public int compareTo(State that) {
      return this.cost - that.cost;
    }

    public Board getBoard() {
      return board;
    }

    public int getMoves() {
      return moves;
    }

    public State getPreState() {
      return preState;
    }
  }

  // test client (see below)
  public static void main(String[] args) {

    // create initial board from file
    int n = StdIn.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = StdIn.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
    StdOut.println(solver.solution());
  }
}