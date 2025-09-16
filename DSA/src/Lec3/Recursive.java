package Lec3;

public class Recursive {
  public static int recursive(int a, int n) {
    if(n == 0) {
      return 1;
    }
    int recurve = recursive(a, n / 2);
    if(n % 2 == 0) {
      return recurve * recurve;
    }
    return recurve * recurve * a;
  }

  public static void main(String[] args) {
    int a = 3;
    int n = 4;
    System.out.println(recursive(3, 10));
  }
}
