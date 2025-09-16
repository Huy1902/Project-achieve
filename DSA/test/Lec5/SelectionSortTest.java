package Lec5;

import edu.princeton.cs.algs4.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SelectionSortTest {
  @BeforeEach
  void setUp() {
  }

  @Test
  void testSelectionSort1() {
    int[] test = new int[]{1, 2, 4, 8, 16, 32};
    for (int i = 0; i < test.length; i++) {
      String file = Integer.toString(test[i]) + "Kints.txt";
      In in = new In("C:\\Users\\lymst\\OneDrive\\Desktop\\DSA\\DSA\\src\\main\\resources\\" + file);
      int[] a = in.readAllInts();
      ArrayList<Integer> temp = new ArrayList<>();
      for (int j = 0; j < a.length; ++j) {
        temp.add(a[j]);
      }
      Collections.sort(temp);
      int[] expected = new int[temp.size()];
      for (int j = 0; j < temp.size(); ++j) {
        expected[j] = temp.get(j);
      }
      double start = System.currentTimeMillis();
      SelectionSort.sort(a);
      double end = System.currentTimeMillis();
      assertArrayEquals(expected, a);
      System.out.println(end - start);
    }
  }

  @Test
  void testSelectionSort2() {
    int trial = 1000;
    double sumCoefficient = 0;
    for (int t = 0; t < trial; ++t) {
      int n = 20000;
      int[] a = new int[n];
      for (int i = 0; i < n; ++i) {
        a[i] = StdRandom.uniformInt(1, 10000);
      }
      double start = System.currentTimeMillis();
      SelectionSort.sort(a);
      double end = System.currentTimeMillis();
      sumCoefficient += (end - start) / (n * n);
      //System.out.println(end - start);
    }
    System.out.println(sumCoefficient / trial);
  }

  @Test
  void testSelectionSort3() {
    int trial = 10000;
    double[] resultTrial = new double[trial];
    double[] coefficient1 = new double[trial];
    double[] coefficient2 = new double[trial];
    for (int t = 0; t < trial; ++t) {
      int n = StdRandom.uniformInt(500, 1000);

      ArrayList<Integer> temp = new ArrayList<>();
      for (int i = 0; i < n; ++i) {
        temp.add(StdRandom.uniformInt(1, 10000));
      }
      Collections.sort(temp);

      int[] a = new int[n];
      for (int i = 0; i < n; ++i) {
        a[i] = temp.get(i);
      }
      //StdOut.println(temp);

      double start = System.currentTimeMillis();
      SelectionSort.sort(a);
      double end = System.currentTimeMillis();
      resultTrial[t] = (end - start);
      coefficient1[t] = (end - start) / (n * n);
      coefficient2[t] = (end - start) / n;
      //System.out.println(end - start);
    }
    double Coefficient_of_Variation = StdStats.stddev(resultTrial) / StdStats.mean(resultTrial);
    StdOut.print("Coefficient_of_Variation: " + Coefficient_of_Variation
            + "\nMean: " + StdStats.mean(resultTrial)
            + "\nStandard_deviation: " + StdStats.stddev(resultTrial)
            + "\nCoefficient1: " + StdStats.stddev(coefficient1)
            + "\nCoefficient2: " + StdStats.stddev(coefficient2));
  }


  @Test
  void testSelectionSort4() {
    int trial = 10000;
    double[] resultTrial = new double[trial];
    double[] coefficient1 = new double[trial];
    double[] coefficient2 = new double[trial];
    for (int t = 0; t < trial; ++t) {
      int n = StdRandom.uniformInt(500, 1000);

      ArrayList<Integer> temp = new ArrayList<>();
      for (int i = 0; i < n; ++i) {
        temp.add(StdRandom.uniformInt(1, 10000));
      }
      Collections.sort(temp);

      int[] a = new int[n];
      for (int i = n - 1; i >= 0; --i) {
        a[n - i - 1] = temp.get(i);
      }
      //StdOut.println(temp);

      double start = System.currentTimeMillis();
      SelectionSort.sort(a);
      double end = System.currentTimeMillis();
      resultTrial[t] = (end - start);
      coefficient1[t] = (end - start) / (n * n);
      coefficient2[t] = (end - start) / n;
      //System.out.println(end - start);
    }
    double Coefficient_of_Variation = StdStats.stddev(resultTrial) / StdStats.mean(resultTrial);
    StdOut.print("Coefficient_of_Variation: " + Coefficient_of_Variation
            + "\nMean: " + StdStats.mean(resultTrial)
            + "\nStandard_deviation: " + StdStats.stddev(resultTrial)
            + "\nCoefficient1: " + StdStats.stddev(coefficient1)
            + "\nCoefficient2: " + StdStats.stddev(coefficient2));
  }

  @Test
  void testSelectionSort5() {
    int trial = 10000;
    double[] resultTrial = new double[trial];
    double[] coefficient1 = new double[trial];
    double[] coefficient2 = new double[trial];
    for (int t = 0; t < trial; ++t) {
      int n = 10000;//StdRandom.uniformInt(5000, 10000);
      int[] a = new int[n];
      int uniform = StdRandom.uniformInt(1, 10000);
      for (int i = n - 1; i >= 0; --i) {
        a[i] = uniform;
      }
      //StdOut.println(temp);

      double start = System.currentTimeMillis();
      //
      SelectionSort.sort(a);
      //
      double end = System.currentTimeMillis();
      resultTrial[t] = (end - start);
      coefficient1[t] = (end - start) / (n * n);
      coefficient2[t] = (end - start) / n;
      //System.out.println(end - start);
    }
    double Coefficient_of_Variation = StdStats.stddev(resultTrial) / StdStats.mean(resultTrial);
    StdOut.print("Coefficient_of_Variation: " + Coefficient_of_Variation
            + "\nMean: " + StdStats.mean(resultTrial)
            + "\nStandard_deviation: " + StdStats.stddev(resultTrial)
            + "\nCoefficient1: " + StdStats.stddev(coefficient1)
            + "\nCoefficient2: " + StdStats.stddev(coefficient2));
  }
}