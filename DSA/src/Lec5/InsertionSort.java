package Lec5;

public class InsertionSort {
  public static void sort(int[] a) {
    for(int i = 1 ; i < a.length ; i++) {
      int key = a[i];
      int j = i - 1;

      //do swap operation: half exchange improvement
      while(j >= 0 && a[j] > key) {
        a[j + 1] = a[j];
        j--;
      }
      // insert key into correct position
      a[j + 1] = key;
    }
  }
}
