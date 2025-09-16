package Lec8.JesseAndCookies;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import static java.lang.Math.min;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

  /*
   * Complete the 'cookies' function below.
   *
   * The function is expected to return an INTEGER.
   * The function accepts following parameters:
   *  1. INTEGER k
   *  2. INTEGER_ARRAY A
   */
  static class PriorityQueue {
    private int[] heap;
    private int size;
    private int capacity;
    public PriorityQueue(List<Integer> A) {
      int n = A.size();
      this.heap = new int[n + 1];
      this.size = n;
      for(int i = 0; i < n; ++i) {
        heap[i + 1] = A.get(i);
      }
      for(int i = n / 2; i >= 1; --i) {
        sink(i);
      }
    }
    public void insert(int value) {
      heap[++size] = value;
      swim(size);
    }
    public int delMin() {
      swap(1, size--);
      sink(1);
      return heap[size + 1];
    }
    public int getMin() {
      return heap[1];
    }
    public int getSize() {
      return size;
    }
    public int getCapacity() {
      return capacity;
    }
    public boolean isEmpty() {
      return size == 0;
    }

    private boolean greater(int i, int j) {
      return heap[i] > heap[j];
    }

    private void swap(int i, int j) {
      int temp = heap[i];
      heap[i] = heap[j];
      heap[j] = temp;
    }

    private void sink (int k) {
      while(2 * k <= size) {
        int j = 2 * k;
        if(j < size && greater(j, j + 1)) {
          ++j;
        }
        if(!greater(k, j)) {
          break;
        }
        swap(k, j);
        k = j;
      }
    }

    private void swim(int k) {
      while(k > 1 && greater(k / 2, k)) {
        swap(k / 2, k);
        k /= 2;
      }
    }
  }
  public static int cookies(int k, List<Integer> A) {
    // Write your code here
    PriorityQueue pq = new PriorityQueue(A);
    int count = 0;
    while(!pq.isEmpty() && pq.getMin() < k) {
      int temp1 = pq.delMin();
      int temp2 = pq.delMin();
      pq.insert(temp1 + temp2 * 2);
      ++count;
    }
    return min(count, -1);
  }
}

public class Solution {
  public static void main(String[] args) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

    String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

    int n = Integer.parseInt(firstMultipleInput[0]);

    int k = Integer.parseInt(firstMultipleInput[1]);

    List<Integer> A = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

    int result = Result.cookies(k, A);

    bufferedWriter.write(String.valueOf(result));
    bufferedWriter.newLine();

    bufferedReader.close();
    bufferedWriter.close();
  }
}
