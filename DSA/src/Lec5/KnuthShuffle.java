package Lec5;

import edu.princeton.cs.algs4.StdRandom;

public class KnuthShuffle {
  public static void exchange(Object[] a, int i, int j) {
    Object temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
  public static void shuffle(Object[] a) {
    int n = a.length;
    for(int i = 0; i < n; ++i) {
      // uniform return [0, i + 1) => 0 -> i
      int r = StdRandom.uniform(i + 1);
      exchange(a, i, r);
    }
  }
}
