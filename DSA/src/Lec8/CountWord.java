package Lec8;

import java.util.Scanner;
import java.util.TreeMap;

public class CountWord {
  public static void main(String[] args) {
    TreeMap<String, Integer> map = new TreeMap<>();
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter a line of text:");
    String inputLine = scanner.nextLine();
    String[] words = inputLine.trim().split("\\s+");

    for (String word : words) {
//      System.out.println(word);
      if(map.containsKey(word)) {
            map.put(word, map.get(word) + 1);
      } else {
        map.put(word, 1);
      }
    }

    for(String key : map.keySet()) {
      System.out.println(key + " " + map.get(key));
    }
  }
}
