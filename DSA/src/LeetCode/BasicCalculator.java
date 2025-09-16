package LeetCode;

import java.util.Stack;

public class BasicCalculator {
  static class Solution {
    public int calculate(String s) {
      Stack<Integer> value = new Stack<>();
      int countValue = 0;
      int num = 0;
      int sign = 1;
      for(int i = 0; i < s.length(); ++i) {
        char c = s.charAt(i);
        if('0' <= c && c <= '9') {
          num = num * 10 + (c - '0');
          continue;
        }
        if(c == '+' || c == '-') {
          countValue += num * sign;
          num = 0;
          if(c == '+') {
            sign = 1;
          }
          else {
            sign = -1;
          }
          continue;
        }
        if(c == '(') {
          value.push(countValue);
          value.push(sign);
          countValue = 0;
          sign = 1;
          continue;
        }
        if(c == ')')
        {
          countValue += num * sign;
          sign = value.pop();
          countValue = sign * countValue + value.pop();
          num = 0;
          sign = 1;
        }
      }
      return countValue + num * sign;
    }
  }

  public static void main(String[] args) {
    String test = "1 + 1";
    Solution sol = new Solution();
    System.out.println(sol.calculate(test));
  }
}
