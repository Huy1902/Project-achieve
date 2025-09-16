package Lec6;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

  @Test
  public void testSortedArray() {
    int[] array = {1, 2, 3, 4, 5};
    int[] original = array.clone();
    Lec6.MergeSort.sort(array);
    assertArrayEquals(original, array);
  }

  @Test
  public void testReversedArray() {
    int[] array = {5, 4, 3, 2, 1};
    int[] expected = {1, 2, 3, 4, 5};
    Lec6.MergeSort.sort(array);
    assertArrayEquals(expected, array);
  }

  @Test
  public void testRandomArray() {
    int[] array = {4, 1, 3, 5, 2};
    int[] expected = {1, 2, 3, 4, 5};
    Lec6.MergeSort.sort(array);
    assertArrayEquals(expected, array);
  }

  @Test
  public void testEmptyArray() {
    int[] array = {};
    int[] expected = {};
    Lec6.MergeSort.sort(array);
    assertArrayEquals(expected, array);
  }

  @Test
  public void testOneElementArray() {
    int[] array = {1};
    int[] expected = {1};
    Lec6.MergeSort.sort(array);
    assertArrayEquals(expected, array);
  }

  @Test
  void testMergeSortWithAscendingOrderInput() {
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
      Lec6.MergeSort.sort(a);
      double end = System.currentTimeMillis();
      assertArrayEquals(expected, a);
      System.out.println(end - start);
    }
  }

  @Test
  void testMergeSortWithRandomInput() {
    int trial = 1000;
    double sumCoefficient = 0;
    for (int t = 0; t < trial; ++t) {
      int n = 20000;
      int[] a = new int[n];
      for (int i = 0; i < n; ++i) {
        a[i] = StdRandom.uniformInt(1, 10000);
      }
      double start = System.currentTimeMillis();
      Lec6.MergeSort.sort(a);
      double end = System.currentTimeMillis();
      sumCoefficient += (end - start) / (n * Math.log(n));
      //System.out.println(end - start);
    }
    System.out.println(sumCoefficient / trial);
  }

  @Test
  void testMergeSortWithPresortedInput() {
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
      Lec6.MergeSort.sort(a);
      double end = System.currentTimeMillis();
      resultTrial[t] = (end - start);
      coefficient1[t] = (end - start) / (n * Math.log(n));
      coefficient2[t] = (end - start) / n;
      //System.out.println(end - start);
    }
    double Coefficient_of_Variation = StdStats.stddev(resultTrial) / StdStats.mean(resultTrial);
    StdOut.print("Coefficient_of_Variation: " + Coefficient_of_Variation
            + "\nMean: " + StdStats.mean(resultTrial)
            + "\nStandard_deviation: " + StdStats.stddev(resultTrial)
            + "\nCoefficient1(nlogn): " + StdStats.stddev(coefficient1)
            + "\nCoefficient1(n): " + StdStats.stddev(coefficient2)
            + "\n");
  }


  @Test
  void testMergeSortWithReverseOrderInput() {
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
      Lec6.MergeSort.sort(a);
      double end = System.currentTimeMillis();
      resultTrial[t] = (end - start);
      coefficient1[t] = (end - start) / (n * Math.log(n));
      coefficient2[t] = (end - start) / n;
      //System.out.println(end - start);
    }
    double Coefficient_of_Variation = StdStats.stddev(resultTrial) / StdStats.mean(resultTrial);
    StdOut.print("Coefficienet_of_Variation: " + Coefficient_of_Variation
            + "\nMean: " + StdStats.mean(resultTrial)
            + "\nStandard_dviation: " + StdStats.stddev(resultTrial)
            + "\nCoefficient1(nlogn): " + StdStats.stddev(coefficient1) / StdStats.mean(coefficient1)
            + "\nCoefficient1(n): " + StdStats.stddev(coefficient2) / StdStats.mean(coefficient2)
            + "\n");
  }

  @Test
  void testMergeSortWithIdenticalElementsInput() {
    int trial = 10000;
    double[] resultTrial = new double[trial];
    double[] coefficient1 = new double[trial];
    double[] coefficient2 = new double[trial];
    for (int t = 0; t < trial; ++t) {
      int n = StdRandom.uniformInt(500, 1000);
      int[] a = new int[n];
      int uniform = StdRandom.uniformInt(1, 10000);
      for (int i = n - 1; i >= 0; --i) {
        a[i] = uniform;
      }
      //StdOut.println(temp);

      double start = System.currentTimeMillis();
      Lec6.MergeSort.sort(a);
      double end = System.currentTimeMillis();
      resultTrial[t] = (end - start);
      coefficient1[t] = (end - start) / (n * Math.log(n));
      coefficient2[t] = (end - start) / n;
    }
    double Coefficient_of_Variation = StdStats.stddev(resultTrial) / StdStats.mean(resultTrial);
    StdOut.print("Coefficient_of_Variation: " + Coefficient_of_Variation
            + "\nMean: " + StdStats.mean(resultTrial)
            + "\nStandard_deviation: " + StdStats.stddev(resultTrial)
            + "\nCoefficient1(nlogn): " + StdStats.stddev(coefficient1) / StdStats.mean(coefficient1)
            + "\nCoefficient1(n): " + StdStats.stddev(coefficient2) / StdStats.mean(coefficient2)
            + "\n");
  }
}