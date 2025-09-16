package Lec5;

public class LIS {
  private static int[] a;
  private static int maxLen = 0;

  public static int recursive1(int i) {
    if(i == 0) {
      return 1;
    }
    int ans = 1;
    for(int j = i - 1; j >= 0; j--) {
      if(a[j] < a[i]) {
        ans = Math.max(ans, recursive1(j) + 1);
      }
    }
    return ans;
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
    System.out.println(recursive1(a.length - 1));
  }
}
