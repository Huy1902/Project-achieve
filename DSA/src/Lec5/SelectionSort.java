package Lec5;

import edu.princeton.cs.algs4.StdRandom;

public class SelectionSort {
  public static void sort(int[] a) {
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
  }
}

