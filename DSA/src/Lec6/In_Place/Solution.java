package Lec6.In_Place;

import java.io.*;
import java.util.*;

public class Solution {
  public static void sort(int[] a, int l, int r) {
    if(l >= r) {
      return;
    }
    int p = partition(a, l, r);
    for(int j = 0; j < a.length; ++j) {
      System.out.print(a[j] + " ");
    }
    System.out.println();
    sort(a, l, p - 1);
    sort(a, p + 1, r);
  }

  public static int partition(int[] a, int l, int r) {
    int pivot = a[r];
    int i = l;
    for(int j = l; j <= r - 1; ++j) {
      if(a[j] <= pivot) {
        swap(a, i, j);
        ++i;
      }
    }
//    System.out.printf("%d %d %d %d\n",l, r, pivot, i);
    swap(a, i, r);
    return i;
  }

  public static void swap(int[] a, int i, int j) {
    int temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  public static void main(String[] args) {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int[] a = new int[n];
    for(int i = 0; i < n; ++i) {
      a[i] = sc.nextInt();
    }
    sc.close();

    sort(a, 0, n - 1);
  }
}