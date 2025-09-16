package Lec11;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class FindTheRunningMedian {
  private static class Node implements Comparable<Node> {
    private final double data;
    private final double index;

    public Node(double data, double index) {
      this.data = data;
      this.index = index;
    }

    public double getData() {
      return data;
    }

    public double getIndex() {
      return index;
    }

    @Override
    public int compareTo(Node o) {
      int compare = Double.compare(data, o.data);
      if (compare == 0) return Double.compare(index, o.index);
      return compare;
    }
  }

  public static List<Double> runningMedian(List<Integer> a) {
    TreeSet<Node> tree = new TreeSet<>();
    List<Double> result = new ArrayList<Double>();
    Node median_low = null;
    Node median_high = null;
    for (int i = 0; i < a.size(); ++i) {
      System.out.println(i + " " + a.get(i));
      Node newNode = new Node(a.get(i), i);
      if (median_low == null) {
        tree.add(newNode);
        median_low = newNode;
        median_high = newNode;
        result.add(median_low.data);
      } else {
        tree.add(newNode);
        if (tree.size() % 2 == 0) {
          if (newNode.getData() >= median_low.getData()) {
            median_high = tree.higher(median_low);
          } else if (newNode.getData() < median_high.getData()) {
            median_low = tree.lower(median_high);
          }
        } else {
          if (newNode.getData() >= median_high.getData()) {
            median_low = median_high;
          } else if (newNode.getData() < median_low.getData()) {
            median_high = median_low;
          } else {
            median_low = newNode;
            median_high = newNode;
          }
        }
        result.add((median_low.getData() + median_high.getData()) / 2);
      }
      System.out.println(median_low.getData() + " " + median_high.getData());
    }
    return result;
  }

  public static void main(String[] args) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Paths.get("src/main/resources/output.txt").toFile()));

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

    List<Double> result = runningMedian(a);

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
