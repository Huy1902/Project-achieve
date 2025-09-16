package Lec5;

public class BubbleSort {
  public static void sort(int[] a) {
    int n = a.length;
    for(int i = 0; i < n - 1; ++i) {
      boolean swapped = false;
      for(int j = 0; j < n - i - 1; ++j) {
        if(a[j] > a[j + 1]) {
          exchange(a, j, j + 1);
          swapped = true;
        }
      }
      if(!swapped) {
        break;
      }
    }
  }
  private static void exchange(int[] a, int i, int j) {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
}
