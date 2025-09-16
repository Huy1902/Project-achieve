package Lec8;

import edu.princeton.cs.algs4.StdIn;

import java.util.Scanner;

public class HeapSort {
  public static void sort(Comparable[] pq) {

    int n = pq.length;

    for(int k = n / 2; k >= 1; --k) {
      sink(pq, k, n);
    }

    int k = n;
    while(k > 1) {
      exchange(pq, 1, k--);
      sink(pq, 1, k);
    }
  }

  private static void sink(Comparable[] pq, int k, int n) {
    while (2 * k <= n) {
      int j = 2 * k;
      if(j < n && less(pq, j, j+1)) {
        ++j;
      }
      if(!less(pq, k, j)) {
        break;
      }
      exchange(pq, k, j);
      k = j;
    }
  }

  private static boolean less(Comparable[] pq, int i, int j) {
    return pq[i - 1].compareTo(pq[j - 1]) < 0;
  }

  private static void exchange(Comparable[] pq, int i, int j) {
    Comparable temp = pq[i - 1];
    pq[i - 1] = pq[j - 1];
    pq[j - 1] = temp;
  }

  public static void main(String[] args) {
    String[] a = StdIn.readAllStrings();
    HeapSort.sort(a);
    for(String s : a) {
      System.out.println(s);
    }
  }
}

class Solution {

  public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);
    String A=sc.next();
    int n = A.length();
    for(int i = 0; i <= n / 2; ++i) {
      if(A.charAt(i) != A.charAt(n - i - 1)) {
        System.out.print("No");
        return;
      }
    }
    System.out.print("Yes");
    /* Enter your code here. Print output to STDOUT. */

  }
}




