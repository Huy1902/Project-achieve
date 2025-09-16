package Lec10;


import java.util.ArrayList;
import java.util.List;

public class Tree23 <Key extends Comparable<Key>, Value> {
  private Node root;

  private class Node {
    private List<Key> keys = new ArrayList<Key>();
    private List<Value> values = new ArrayList<>();
    private Node parent;
    private List<Node> children = new ArrayList<Node>();
    public Node(Key key, Value value) {

    }
  }
}
