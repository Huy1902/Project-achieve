package Lec5;

public class MaxAreaRec {
  private static int[] a;
  private static int maxLen = 0;

  private class Rectangle {
    int length;
    int width;
  }

  private static int recursive(int i) {
    return 0;
  }
  public static void main(String[] args) {
    a = new int[]{1, 4, 7, 6, 10};
    int[] dp = new int[a.length];
    dp[0] = 1;
    for(int i = 1; i < a.length; i++) {
      dp[i] = 1;
      for(int j = i - 1; j >= 0; j--) {
        if(a[j] < a[i]) {
          dp[i] = Math.max(dp[j] + 1, dp[i]);
        }
      }
      if(dp[i] > maxLen) {
        maxLen =  dp[i];
      }
    }
    System.out.println(recursive(a.length - 1));
  }
}
