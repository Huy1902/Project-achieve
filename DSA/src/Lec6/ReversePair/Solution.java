package Lec6.ReversePair;

import java.util.Arrays;

public class Solution {
  public static int upperBound(int[] array, long value, int i, int j) {
    int low = i;
    int high = j;

    while (low < high) {
      int mid = low + (high - low) / 2;
      if ((long)array[mid] <= (long)value) {
        low = mid + 1;
      } else {
        high = mid;
      }
    }
    return low;
  }


  public static int merge(int[] a, int[] aux, int low, int mid, int high) {
    System.out.println("-----------------");
    int i = low, j = mid + 1;
    int count = 0;
    for (int k = mid + 1; k <= high; k++) {
      if ((long)aux[k] * 2 >= (long)aux[mid]) {

        continue;
      }
      if ((long)aux[k] * 2 < (long)aux[low]) {
        count += mid - low + 1;
        continue;
      }
      int pos = upperBound(aux, (long)aux[k] * 2, low, mid);
      System.out.printf("\npos: %d\n", pos);
      count += mid + 1 - pos;
    }

    for (int k = low; k <= mid; k++) {
      System.out.printf("%d ", aux[k]);
    }
    System.out.println();
    for (int k = mid + 1; k <= high; k++) {
      System.out.printf("%d ", aux[k]);
    }
    System.out.println();
    System.out.println("-----------------");
    System.out.printf("count: %d\n-----------------\n", count);
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

    return count;
  }

  public static int sort(int[] a, int[] aux, int low, int high) {
    if (high <= low) return 0;
    int mid = low + (high - low) / 2;
    int count = 0;
    count += sort(aux, a, low, mid);
    count += sort(aux, a, mid + 1, high);

//    if (mid + 1 <= high && aux[mid] <= aux[mid + 1]) {
//      for (int i = low; i <= high; i++) {
//        a[i] = aux[i];
//      }
//      return 0;
//    }
    count += merge(a, aux, low, mid, high);
    return count;
  }

  public int reversePairs(int[] a) {
    int[] aux = a.clone();
    return sort(a, aux, 0, a.length - 1);
  }

  public static void main(String[] args) {
    Solution solution = new Solution();
    System.out.println(solution.reversePairs(new int[]{1, 2, 1, 2, 1}));
  }
}