package Lec6.FindtheMedian;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

  /*
   * Complete the 'findMedian' function below.
   *
   * The function is expected to return an INTEGER.
   * The function accepts INTEGER_ARRAY arr as parameter.
   */
  public static int partition(List<Integer> arr, int l, int r) {
    int pivot = arr.get(l);
    int i = l + 1;
    int j = r;
    while(true) {
      while(i < r && arr.get(i) <= pivot) {
        ++i;
      }
      while(j > l && arr.get(j) > pivot) {
        --j;
      }
      if(i >= j) {
        break;
      }
      swap(arr, i, j);
    }
    swap(arr, l, j);
    return j;
  }

  public static void swap(List<Integer> arr, int i, int j) {
    //System.out.printf("%d %d\n", i, j);
    int temp = arr.get(i);
    arr.set(i, arr.get(j));
    arr.set(j, temp);
  }

  public static int findMedianRecursive(List<Integer> arr, int l, int r) {
    if(l >= r) {
      return l;
    }
    int p = partition(arr, l, r);
    if(arr.size() / 2 == p) {
      return p;
    }
    int temp = findMedianRecursive(arr, l, p - 1);
    if(temp == arr.size() / 2) {
      return temp;
    }
    return findMedianRecursive(arr, p + 1, r);
  }

  public static int findMedian(List<Integer> arr) {
    // Write your code here
    return arr.get(findMedianRecursive(arr, 0, arr.size() - 1));
  }
}

public class Solution {
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    List<Integer> arr = new ArrayList<>();
    for(int i = 0; i < n; ++i) {
      arr.add(sc.nextInt());
    }
    sc.close();
    System.out.println(Result.findMedian(arr));

  }
}
