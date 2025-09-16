package LeetCode;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Four_some {
  static class Solution {
    public List<List<Integer>> fourSum(int[] nums, long target) {
      int n = nums.length;
      Arrays.sort(nums);
      //System.out.println(Arrays.toString(nums));
      List<List<Integer>> ans = new ArrayList<>();
      for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
          long need_find = target - nums[i] - nums[j];
          int l = j + 1;
          int r = n - 1;
          while (l < r) {
            int sum = nums[l] + nums[r];
            if (sum == need_find) {
              ans.add(Arrays.asList(nums[i], nums[j], nums[l], nums[r]));
              while (l < r && nums[l] == nums[l + 1]) {
                ++l;
              }
              while (l < r && nums[r] == nums[r - 1]) {
                --r;
              }
            }
            if (sum < need_find) {
              ++l;
            } else {
              --r;
            }
          }
          while (j < n - 1 && nums[j] == nums[j + 1]) {
            ++j;
          }
        }
        while (i < n - 1 && nums[i] == nums[i + 1]) {
          ++i;
        }
      }
      return ans;
    }
  }

  public static void main(String[] args) {
    int[] nums = new int[]{1, 0, -1, 0, -2, 2};
    Solution sol = new Solution();
    System.out.println(sol.fourSum(nums, 0));
  }
}
