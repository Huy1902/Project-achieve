package Lec4;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Scanner;

public class SelectionSort {
  private static void sort(int[] a) {
    for (int i = 0; i < a.length; ++i) {
      int min = a[i];
      int index = i;
      for (int j = i + 1; j < a.length; ++j) {
        if (a[j] < min) {
          min = a[j];
          index = j;
        }
      }
      a[index] = a[i];
      a[i] = min;
    }
    System.out.println();
  }

  private static void output(int[] a) {
    for (int i = 0; i < a.length; ++i) {
      System.out.printf("%d ",a[i]);
    }
  }

  public static void main(String[] args) {
    int trial = 100;
    double sumCoefficient = 0;
    for(int t = 0; t < trial; ++t){
      int n = StdRandom.uniformInt(20000, 30000);
      int[] a = new int[n];
      for (int i = 0; i < n; ++i) {
        a[i] = StdRandom.uniformInt(1, 10000);
      }
      double start = System.currentTimeMillis();
      sort(a);
      double end = System.currentTimeMillis();
      sumCoefficient += (end - start) / (n * n);
      System.out.println((end - start) / (n * n));
    }
    System.out.println(sumCoefficient / trial);
  }
}
