package Lec6;

import java.util.Arrays;

public class MergeSort {
  public static void merge(int[] a, int[] aux, int low, int mid, int high) {
    int i = low, j = mid + 1;
    for (int k = low; k <= high; k++) {
      if (i > mid) {
        a[k] = aux[j++];
        continue;
      }
      if (j > high) {
        a[k] = aux[i++];
        continue;
      }
      if (aux[i] <= aux[j]) {
        a[k] = aux[i++];
        continue;
      }
      a[k] = aux[j++];
    }
  }

  public static void sort(int[] a, int[] aux, int low, int high) {
    if (high <= low) return;
    int mid = low + (high - low) / 2;
    sort(aux, a, low, mid);
    sort(aux, a, mid + 1, high);

    if (mid + 1 <= high && aux[mid] <= aux[mid + 1]) {
      for(int i = low; i <= high; i++ ) {
        a[i] = aux[i];
      }
      return;
    }
    merge(a, aux, low, mid, high);
  }

  public static void sort(int[] a) {
    int[] aux = a.clone();
    sort(a, aux, 0, a.length - 1);
  }

//  public static void main(String[] args) {
//    int[] a = new int[]{9, 3, 2, 1, 5, 4, 6, 8, 7};
//    sort(a);
//    System.out.println(Arrays.toString(a));
//  }
}
