package Lec14.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FarmProtection {

  public static final List<Integer> x_change = Arrays.asList(1, -1, 0, 0, 1, 1, -1, -1);
  public static final List<Integer> y_change = Arrays.asList(0, 0, 1, -1, -1, 1, -1, 1);
  public static boolean is_local_highest = false;

  public static List<List<Boolean>> visited = new ArrayList<>();

  public static boolean validation(List<List<Integer>> matrix, int x, int y) {
    return x >= 0 && x < matrix.size() && y >= 0 && y < matrix.get(0).size();
  }

  public static void dfs(List<List<Integer>> matrix, int x, int y) {
    int current_high = matrix.get(x).get(y);
    visited.get(x).set(y, true);
    if(!is_local_highest) {
      return;
    }
    for(int i = 0; i < 8; ++i) {
      int newX = x + x_change.get(i);
      int newY = y + y_change.get(i);
      if(validation(matrix, newX, newY) && matrix.get(newX).get(newY) != -1) {
        int new_high = matrix.get(newX).get(newY);
        if(new_high == current_high && !visited.get(newX).get(newY)) {
          dfs(matrix, newX, newY);
        }
        else if (new_high > current_high) {
          is_local_highest = false;
          return;
        }
      }
    }
  }

  public static int solution(List<List<Integer>> matrix) {
    int number_of_connected_components = 0;
    for(int i = 0; i < matrix.size(); ++i) {
      for(int j = 0; j < matrix.get(i).size(); ++j) {
        if(!visited.get(i).get(j)) {
          is_local_highest = true;

          dfs(matrix, i, j);

          if(is_local_highest) {
            ++number_of_connected_components;
          }
        }
      }
    }

    return number_of_connected_components;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<List<Integer>> matrix = new ArrayList<>();
    int m, n;
    m = sc.nextInt();
    n = sc.nextInt();
    for(int i = 0; i < m; ++i) {
      matrix.add(new ArrayList<>());
      visited.add(new ArrayList<>());
      for(int j = 0; j < n; ++j) {
        matrix.get(i).add(sc.nextInt());
        visited.get(i).add(false);
      }
    }

    System.out.println(solution(matrix));
  }
}
