package Lec11;

import java.util.*;

public class Result {
  /**
   * Method 1
   *
   * @param arr
   * @param brr
   * @return
   */
//  public static List<Integer> missingNumbers(List<Integer> arr, List<Integer> brr) {
//    HashMap<Integer, Integer> map = new HashMap<>();
//    for(Integer i : arr) {
//      map.put(i, map.getOrDefault(i, 0) + 1);
//    }
//    for(Integer i : brr) {
//      map.put(i, map.getOrDefault(i, 0) - 1);
//    }
//
//    List<Integer> res = new ArrayList<>();
//    for(Integer i : map.keySet()) {
//      if(map.get(i) != 0) {
//        res.add(i);
//      }
//    }
//    res.sort(Integer::compareTo);
//    return res;
//  }


  /**
   * Method 2
   * @param arr
   * @param brr
   * @return
   */
//  public static List<Integer> missingNumbers(List<Integer> arr, List<Integer> brr) {
//    // Write your code here
//    arr.sort(Integer::compareTo);
//    brr.sort(Integer::compareTo);
////    for (Integer i : arr) {
////      System.out.print(i + " ");
////    }
////    System.out.println();
////    for (Integer i : brr) {
////      System.out.print(i + " ");
////    }
//    System.out.println();
//    int i = 0;
//    int j = 0;
//    List<Integer> result = new ArrayList<>();
//    while (j < brr.size()) {
////      System.out.print(arr.get(i) + " " + brr.get(j) + "\n");
//      if (i < arr.size() && Objects.equals(arr.get(i), brr.get(j))) {
//        ++i;
//        ++j;
//      } else {
//        result.add(brr.get(j));
//        int missing = brr.get(j);
//        while(j < brr.size() && brr.get(j) == missing) ++j;
//      }
//    }
//    return result;
//  }

  /**
   * Method 3
   * @param arr
   * @param brr
   * @return
   */
//  public static List<Integer> missingNumbers(List<Integer> arr, List<Integer> brr) {
//    int[] a = new int[101];
//    for(int i = 0; i <= 100; ++i) {
//      a[i] = 0;
//    }
//    int min = brr.stream().min(Integer::compare).get();
//    for(Integer i : arr) {
//      a[i - min]++;
//    }
//    for(Integer i : brr) {
//      a[i - min]--;
//    }
//    List<Integer> res = new ArrayList<>();
//    for(int i = 0; i <= 100; ++i) {
//      if(a[i] != 0) {
//        res.add(i + min);
//      }
//    }
//    return res;
//  }
//
//  public static void main(String[] args) {
//
//    List<Integer> arr = Arrays.asList(203, 204, 205, 206, 207, 208, 203, 204, 205, 206);
//    List<Integer> brr = Arrays.asList(203, 204, 204, 205, 206, 207, 205, 208, 203, 206, 205, 206, 204);
//    for(Integer i : missingNumbers(arr, brr)) {
//      System.out.println(i + " ");
//    }
//
//  }
}
