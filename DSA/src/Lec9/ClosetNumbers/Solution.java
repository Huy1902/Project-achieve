package Lec9.ClosetNumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
  public static void merge(Integer[] a, Integer[] aux, int lo, int mid, int hi) {
    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (j > hi) {
        a[k] = aux[i++];
        continue;
      }
      if (i > mid) {
        a[k] = aux[j++];
        continue;
      }
      if (aux[i] <= aux[j]) {
        a[k] = aux[i++];
      }
      a[k] = aux[j++];
    }
  }

  public static void mergeSort(Integer[] a, Integer[] aux, int lo, int hi) {
    if (hi <= lo) {
      return;
    }
    int mid = lo + (hi - lo) / 2;
    mergeSort(aux, a, lo, mid);
    mergeSort(aux, a, mid + 1, hi);
    merge(a, aux, lo, mid, hi);
  }

  public static void sort(Integer[] a) {
    Integer[] aux = a.clone();
    mergeSort(a, aux, 0, a.length - 1);
  }

  public static List<Integer> closestNumbers(List<Integer> arr) {
    // Write your code here
    Integer[] a = new Integer[arr.size()];
    for(int i = 0; i < arr.size(); i++) {
      a[i] = arr.get(i);
    }
    sort(a);
    arr = new ArrayList<>(Arrays.asList(a));
    int minimum = Integer.MAX_VALUE;
    for (int i = 1; i < arr.size(); ++i) {
      if (arr.get(i) - arr.get(i - 1) < minimum) {
        minimum = arr.get(i) - arr.get(i - 1);
      }
    }
    List<Integer> result = new ArrayList<>();
    for (int i = 1; i < arr.size(); ++i) {
      if (arr.get(i) - arr.get(i - 1) == minimum) {
        result.add(arr.get(i - 1));
        result.add(arr.get(i));
      }
    }
    return result;
  }

  public static void main(String[] args) {

  }
}
