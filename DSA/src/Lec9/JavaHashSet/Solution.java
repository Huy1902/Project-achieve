package Lec9.JavaHashSet;

import java.util.*;

public class Solution {

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    int t = s.nextInt();
    String[] pair_left = new String[t];
    String[] pair_right = new String[t];


    for (int i = 0; i < t; i++) {
      pair_left[i] = s.next();
      pair_right[i] = s.next();
    }


//Write your code here
    HashSet<Pair> hs = new HashSet<>();
    for (int i = 0; i < t; i++) {
      hs.add(new Pair(pair_left[i], pair_right[i]));
      System.out.println(hs.size());
    }
  }

  private static class Pair {
    String left;
    String right;

    public Pair(String left, String right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Pair) {
        Pair p = (Pair) o;
        return this.left.equals(p.left) && this.right.equals(p.right);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return this.left.hashCode() + this.right.hashCode();
    }
  }
}