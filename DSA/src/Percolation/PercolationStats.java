package Percolation;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

  private double[] thresholds;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    thresholds = new double[trials];
    for (int i = 0; i < trials; ++i) {
      Percolation test = new Percolation(n);

      while (!test.percolates()) {
        int row = StdRandom.uniformInt(1, n + 1);
        int col = StdRandom.uniformInt(1, n + 1);

        if (!test.isOpen(row, col)) {
          test.open(row, col);
          //System.out.printf("%d %d %n", row, col);
        }
      }
      thresholds[i] = (double) test.numberOfOpenSites() / (n * n);
    }

  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(thresholds);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(thresholds);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - 1.96 * stddev() / Math.sqrt(thresholds.length);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + 1.96 * stddev() / Math.sqrt(thresholds.length);
  }

  // test client (see below)
  public static void main(String[] args) {
    //PercolationStats test = new PercolationStats(StdIn.readInt(), StdIn.readInt());

    PercolationStats test = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    System.out.printf("mean                     = %f%n", test.mean());
    System.out.printf("stddev                   = %f%n", test.stddev());
    System.out.printf("95%% confidence interval  = [%f, %f]%n", test.confidenceLo(), test.confidenceHi());
  }
}

