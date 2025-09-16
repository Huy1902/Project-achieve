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
   * Complete the 'runningMedian' function below.
   *
   * The function is expected to return a DOUBLE_ARRAY.
   * The function accepts INTEGER_ARRAY a as parameter.
   */

  public static List<Double> runningMedian(List<Integer> a) {
    // Write your code here
    List<Double> result = new ArrayList<>();
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a1, a2) -> a2 - a1);
    for(int i = 0; i < a.size(); ++i) {
      if(minHeap.size() <= maxHeap.size()) {
        minHeap.add(a.get(i));
      } else {
        maxHeap.add(a.get(i));
      }
      if(!minHeap.isEmpty() && !maxHeap.isEmpty() && minHeap.peek() < maxHeap.peek()) {
        int temp = minHeap.peek();
        minHeap.add(maxHeap.poll());
        maxHeap.add(temp);
      }
      if((minHeap.size() + maxHeap.size()) % 2 == 1) {
        result.add((double) minHeap.peek());
      } else {
        result.add((double) (minHeap.peek() + maxHeap.peek()) / 2);
      }
    }
    return result;
  }
}

public class Solution {
  public static void main(String[] args) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

    int aCount = Integer.parseInt(bufferedReader.readLine().trim());

    List<Integer> a = IntStream.range(0, aCount).mapToObj(i -> {
              try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
              } catch (IOException ex) {
                throw new RuntimeException(ex);
              }
            })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

    List<Double> result = Result.runningMedian(a);

    bufferedWriter.write(
            result.stream()
                    .map(Object::toString)
                    .collect(joining("\n"))
                    + "\n"
    );

    bufferedReader.close();
    bufferedWriter.close();
  }
}
