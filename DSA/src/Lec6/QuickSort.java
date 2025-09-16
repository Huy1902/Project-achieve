package Lec6;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class QuickSort {
  private static void exchange(int[] a, int i, int j) {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  private static int partition(int[] a, int left, int right) {
    int pivot = a[left];
    int l = left + 1;
    int r = right;
    while (true) {
      //System.out.printf("%d %d %d\n", l, r, pivot);
      while (l < right && a[l] <= pivot) {
        ++l;
      }
      while (r > left && a[r] >= pivot) {
        --r;
      }
      //System.out.printf("%d %d %d\n", l, r, pivot);
      if(l >= r) {
        break;
      }
      exchange(a, l, r);
    }
    exchange(a, r, left);
    //System.out.println(Arrays.toString(a));
    return r;
  }

  private static int partition2(int[] a, int left, int right) {
    int pivot = a[right];
    int l = left;
    int r = right - 1;
    while (true) {
      while (l < right && a[l] <= pivot) {
        ++l;
      }
      while (r > left && a[r] >= pivot) {
        --r;
      }
      if(l >= r) {
        break;
      }
      exchange(a, l, r);
    }
    exchange(a, l, right);
    return  l;
  }

  // This code is wrong. And I know why it is wrong. <3
  private static int partition3(int[] a, int left, int right) {
    int pivot = a[(right +left) / 2];
    int l = left;
    int r = right;
    while (true) {
      while (l < right && a[l] <= pivot) {
        ++l;
      }
      while (r > left && a[r] >= pivot) {
        --r;
      }
      if(l >= r) {
        break;
      }
      exchange(a, l, r);
    }
    return l;
  }

  private static void sort(int[] a, int l, int r) {
    if(l <= r - 1) {
      int p = partition2(a, l, r);
      sort(a, l, p - 1);
      sort(a, p + 1, r);
    }
  }

  public static void sort(int[] a) {
    StdRandom.shuffle(a);
    sort(a, 0, a.length - 1);
  }
}