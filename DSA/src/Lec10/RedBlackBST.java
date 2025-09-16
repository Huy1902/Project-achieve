package Lec10;


import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class RedBlackBST<Key extends Comparable<Key>, Value> {
  private static final boolean RED = true;
  private static final boolean BLACK = false;

  private Node root;

  private class Node {
    Key key;
    Value value;
    Node left, right;
    boolean color;
    private int size;

    public Node(Key key, Value value, boolean color, int size) {
      this.key = key;
      this.value = value;
      this.color = color;
      this.size = size;
    }
  }

  private boolean isRed(Node node) {
    if (node == null) return false;
    return node.color == RED;
  }

  private int size(Node node) {
    if (node == null) return 0;
    return node.size;
  }

  /**
   * initialize RedBlackBST
   */
  public RedBlackBST() {

  }


  private Node rotateRight(Node up) {
    Node down = up.left;
    up.left = down.right;
    down.right = up;
    down.color = up.color;
    up.color = RED;
    down.size = up.size;
    up.size = size(up.left) + size(up.right);
    return down;
  }

  private Node rotateLeft(Node up) {
    Node down = up.right;
    up.right = down.left;
    down.left = up;
    down.color = up.color;
    up.color = RED;
    down.size = up.size;
    up.size = size(up.left) + size(up.right);
    return down;
  }

  private void flipColor(Node middle) {
    middle.color = !middle.color;
    middle.left.color = !middle.left.color;
    middle.right.color = !middle.right.color;
  }

  public void put(Key key, Value value) {
    if (key == null) throw new NullPointerException("key is null");
    if (value == null) throw new NullPointerException("value is null");

    root = put(root, key, value);
    root.color = BLACK;
  }

  private Node put(Node up, Key key, Value val) {
    // Insert into a node at leaf (imagination by 2-3 tree)
    if (up == null) return new Node(key, val, RED, 1);

    int compare = key.compareTo(up.key);
    if (compare == 0) {
      up.value = val;
    } else if (compare < 0) {
      up.left = put(up.left, key, val);
    } else {
      up.right = put(up.right, key, val);
    }

    // re-balance
    // ensure all red link lean leaf so red link lean right will not happen -> reduction complex
    if (isRed(up.right) && !isRed(up.left)) {
      up = rotateLeft(up);
    }

    // fix double continuous red link lean leaf
    if (isRed(up.left) && isRed(up.left.left)) {
      up = rotateRight(up);
    }
    // fix two red link to one node (after rotate right may happen)
    if (isRed(up.left) && isRed(up.right)) {
      flipColor(up);
    }
    up.size = size(up.left) + size(up.right) + 1;
    return up;
  }

  public Key min() {
    return min(root).key;
  }

  private Node min(Node node) {
    if (node.left == null) return node;
    return min(node.left);
  }

  public Key max() {
    return max(root).key;
  }

  private Node max(Node node) {
    if (node.right == null) return node;
    return max(node.right);
  }

  public Iterable<Key> keys() {
    return keys(min(), max());
  }

  public Iterable<Key> keys(Key min, Key max) {
    Queue<Key> queue = new Queue<>();
    keys(root, queue, min, max);
    return queue;
  }

  private void keys(Node node, Queue<Key> queue, Key min, Key max) {
    if (node == null) return;
    int compareMin = min.compareTo(node.key);
    int compareMax = max.compareTo(node.key);
    if (compareMin < 0) keys(node.left, queue, min, max);
    if (compareMin <= 0 && compareMax >= 0) queue.enqueue(node.key);
    if (compareMax > 0) keys(node.right, queue, min, max);
  }

  public Value get(Key key) {
    return get(root, key);
  }

  private Value get(Node node, Key key) {
    while (node != null) {
      int compare = key.compareTo(node.key);
      if (compare == 0) return node.value;
      if (compare < 0) node = node.left;
      else
        node = node.right;
    }
    return null;
  }

  public static void main(String[] args) {
    RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++) {
      String key = StdIn.readString();
      st.put(key, i);
    }
    StdOut.println();
    for (String s : st.keys())
      StdOut.println(s + " " + st.get(s));
    StdOut.println();
  }
}
