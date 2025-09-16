import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ThreeSum {
  public static List<List<Integer>> threeSum(int[] nums) {
    int n = nums.length;

    Arrays.sort(nums);
    System.out.print(nums.toString());
    List<List<Integer>> result = new ArrayList<>();
    int check = Integer.MIN_VALUE;
    for (int i = 0; i < n - 2; ++i) {
      if (i > 0 && nums[i] == nums[i - 1]) {
        continue;
      }

      int num = nums[i];
      int left = i + 1;
      int right = n - 1;
      while (left < right) {
        int sum = num + nums[left] + nums[right];

        if (sum == 0) {
          List<Integer> temp = new ArrayList<>();
          temp.add(num);
          temp.add(nums[left]);
          temp.add(nums[right]);

          result.add(temp);
          while (left < right && nums[left] == nums[left + 1]) {
            ++left;
          }
          while (right > left && nums[right] == nums[right - 1]) {
            --right;
          }
          ++left;
          --right;
        } else {
          if (sum < 0) {
            ++left;
          } else {
            --right;
          }
        }
      }
    }
    return result;
  }

  public static void main(String[] args) {
    In in = new In("C:\\Users\\lymst\\IdeaProjects\\DSA\\8Kints.txt"); // tạo luồng đọc từ file
    int[] a = in.readAllInts();  // đọc toàn bộ file vào mảng a
    // xử lý dữ liệu trong mảng a
    List<List<Integer>> lists = threeSum(a);
    for(List<Integer> list : lists) {
      for(Integer element : list) {
        System.out.printf("%d ", element);
      }
      System.out.println();
    }
    //StdArrayIO.print(a); // in mảng ra màn hình
  }
}