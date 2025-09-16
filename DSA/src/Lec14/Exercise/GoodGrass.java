package Lec14.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GoodGrass {

  public static final List<Integer> x_change = Arrays.asList(1, -1, 0, 0);
  public static final List<Integer> y_change = Arrays.asList(0, 0, 1, -1);

  public static boolean validation(List<List<Character>> matrix, int x, int y) {
    return x >= 0 && x < matrix.size() && y >= 0 && y < matrix.get(0).size();
  }

  public static void dfs(List<List<Character>> matrix, int x, int y) {
    for(int i = 0; i < 4; ++i) {
      int newX = x + x_change.get(i);
      int newY = y + y_change.get(i);

      if(validation(matrix, newX, newY) && matrix.get(newX).get(newY) == '#') {
        matrix.get(newX).set(newY, '.');
        dfs(matrix, newX, newY);
      }

    }
  }

  public static int solution(List<List<Character>> matrix) {
    int number_of_connected_components = 0;
    for(int i = 0; i < matrix.size(); ++i) {
      for(int j = 0; j < matrix.get(i).size(); ++j) {
        if(matrix.get(i).get(j) == '#') {
          ++number_of_connected_components;
          dfs(matrix, i, j);
        }
      }
    }

    return number_of_connected_components;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    List<List<Character>> matrix = new ArrayList<>();
    int m, n;
    m = sc.nextInt();
    n = sc.nextInt();
    for(int i = 0; i < m; ++i) {
      matrix.add(new ArrayList<>());
      String s = sc.next();
      for(int j = 0; j < n; ++j) {
        matrix.get(i).add(s.charAt(j));
        System.out.print(matrix.get(i).get(j) + " ");
      }
    }

    System.out.println(solution(matrix));
  }
}
