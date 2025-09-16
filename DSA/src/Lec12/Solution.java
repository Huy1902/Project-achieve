package Lec12;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

  /*
   * Complete the 'connectedCell' function below.
   *
   * The function is expected to return an INTEGER.
   * The function accepts 2D_INTEGER_ARRAY matrix as parameter.
   */
  static List<Integer> row = Arrays.asList(-1, 1, 0, 0);
  static List<Integer> col = Arrays.asList(0, 0, -1, 1);
//  static List<List<Boolean>> marked;

  public static boolean validate(List<List<Integer>> matrix, int x, int y) {
    return 0 <= x && x < matrix.get(0).size() && 0 <= y && y < matrix.get(0).size();
  }
  static int count = 0;

  public static void dfs(List<List<Integer>> matrix, int x, int y) {
    if (matrix.get(x).get(y) == 0) {
      return;
    }
    for(int i = 0; i < row.size(); ++i) {
      for(int j = 0; j < col.size(); ++j) {
        int new_row = row.get(i) + x;
        int new_col = col.get(j) + y;
        if(matrix.get(x).get(y) == 1 && validate(matrix, new_row, new_col)) {
          dfs(matrix, new_row, new_col);
        }
      }
    }
  }

  public static int connectedCell(List<List<Integer>> matrix) {
    int m = matrix.size();
    int n = matrix.get(0).size();
//    marked = new ArrayList<>();
//
//    for(int i = 0; i < m; i++) {
//      marked.add(new ArrayList<>());
//    }
    // Write your code here
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        count = 0;
      }
    }
    return 0;
  }

}

public class Solution {
  public static void main(String[] args) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

    int n = Integer.parseInt(bufferedReader.readLine().trim());

    int m = Integer.parseInt(bufferedReader.readLine().trim());

    List<List<Integer>> matrix = new ArrayList<>();

    IntStream.range(0, n).forEach(i -> {
      try {
        matrix.add(
                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
        );
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });

    int result = Result.connectedCell(matrix);

    bufferedWriter.write(String.valueOf(result));
    bufferedWriter.newLine();

    bufferedReader.close();
    bufferedWriter.close();
  }
}
