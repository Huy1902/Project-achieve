package Lec9;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

public class BST <Key extends Comparable<Key>, Value> {
  private Node root;

  private class Node {
    private Key key;
    private Value value;
    private Node left, right;
    private int size;

    public Node(Key key, Value value, int size) {
      this.key = key;
      this.value = value;
      this.size = size;
    }
  }
  /**
   * Initializes a tree
   */
  public BST() {

  }

  /**
   *
   * @return {@code true} if tree is empty; {@code false} otherwise
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return size of tree
   */
  public int size() {
    return size(root);
  }

  /**
   * @param cur: current Node
   * @return size of that node
   */
  private int size(Node cur) {
    if(cur == null) return 0;
    return cur.size;
  }

  /**
   * Search in short
   * Return the value of key
   *
   * @param key the key need to get
   * @return the value of rh given key if the ky is in the tree
   * and {@code null} if the key is not in tree
   */
  public Value get(Key key) {
    return get(root, key);
  }

  private Value get(Node root, Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    if(root == null) return null;
    int compare = key.compareTo(root.key);
    if(compare == 0) return root.value;
    if(compare < 0) return get(root.left, key);
    // We can ensure compare larger than zero.
    return get(root.right, key);
  }

  /**
   * Insert in short
   * Throw exception if key is null or value is null
   * Insert new key, key in tree then reset value; key
   * not in tree then add new node
   *
   * @param key Key want to put in tree
   * @param value value associate with key
   */
  public void put(Key key, Value value) {
    if(key == null) throw new IllegalArgumentException("null key");
    if(value == null) {
      throw new IllegalArgumentException("null value");
    }
    root = put(root, key, value);
  }

  private Node put(Node root, Key key, Value value) {
    if(root == null) return new Node(key, value, 1);
    int compare = key.compareTo(root.key);
    if(compare < 0) root.left = put(root.left, key, value);
    if(compare > 0) root.right = put(root.right, key, value);
    if(compare == 0) root.value = value;
    root.size = 1 + size(root.left) + size(root.right);
    return root;
  }

  /**
   * find the largest key which is smaller than given key
   *
   * @param key given key
   * @return    null if it cannot be found
   */
  public Key floor(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    if(isEmpty()) throw new NoSuchElementException("empty tree");
    return floor(root, key);
  }

  private Key floor(Node root, Key key) {
    if(root == null) return null;
    int compare = key.compareTo(root.key);
    if(compare < 0) return floor(root.left, key);
    if(compare == 0) return root.key;
    Key rightSide = floor(root.right, key);
    if(rightSide != null) return rightSide;
    return root.key;
  }

  /**
   * Find the smallest key which is larger than given key
   *
   * @param key  need to find ceiling value
   * @return     if it cannot find then null
   */
  public Key ceiling(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    if(isEmpty()) throw new NoSuchElementException("empty tree");
    return ceiling(root, key);
  }

  private Key ceiling(Node root, Key key) {
    if(root == null) return null;
    int compare = key.compareTo(root.key);
    if(compare > 0) return ceiling(root.right, key);
    if(compare == 0) return root.key;
    Key leftSide = ceiling(root.left, key);
    if(leftSide != null) return leftSide;
    return root.key;
  }

  /**
   * Return number of key in tree is less than given key
   *
   * @param key given key
   * @return number of key less than given key in tree
   */
  public int rank(Key key) {
    return rank(key, root);
  }

  private int rank(Key key, Node root) {
    if(root == null) return 0;
    int compare = key.compareTo(root.key);
    if(compare == 0) return size(root.left);
    if(compare > 0) return 1 + size(root.left) + rank(key, root.right);
    return rank(key, root.left);
  }

  /**
   * Return Key which has give rank
   *
   * @param rank rank need to find
   * @return Key has give rank
   */
  public Key select(int rank) {
    if(rank < 0 || rank >= size()) {
      throw new IllegalArgumentException("invalid rank");
    }
    return select(root, rank);
  }

  private Key select(Node root, int rank) {
    if(root == null) return null;
    int leftSize = size(root.left);
    if(leftSize < rank) {
      return select(root.right, rank - leftSize - 1);
    }
    if(leftSize > rank) {
      return select(root.left, rank);
    }
    return root.key;
  }

  /**
   * return all keys of tree when inorder traversal
   * @return Iterable of all keys we walk through when inorder traversal
   */
  public Iterable<Key> keys() {
    Queue<Key> q = new Queue<Key>();
    inorder(root, q);
    return q;
  }

  private void inorder(Node root, Queue<Key> q) {
    if(root == null) return;
    inorder(root.left, q);
    q.enqueue(root.key);
    inorder(root.right, q);
  }

  /**
   * Return minimum key in tree
   *
   * @return minimum key
   */
  public Key min() {
    if(isEmpty()) throw new NoSuchElementException("empty tree");
    return min(root).key;
  }

  private Node min(Node root) {
    if(root.left == null) return root;
    return min(root.left);
  }

  /**
   * Delete node with min key in tree
   */
  public void deleteMin() {
    deleteMin(root);
  }

  private Node deleteMin(Node root) {
    if(root.left == null) return root.right;
    root.left = deleteMin(root.left);
    root.size = size(root.left) + size(root.right) + 1;
    return root;
  }

  public void delete(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    delete(root, key);
  }

  private Node delete(Node root, Key key) {
    if(root == null) return null;
    int compare = key.compareTo(root.key);
    if(compare < 0) root.left = delete(root.left, key);
    if(compare > 0) root.right = delete(root.right, key);
    if(compare == 0) {
      if(root.left == null) return root.right;
      if(root.right == null) return root.left;
      Node temp = root;
      root = min(root);
      root = deleteMin(temp.right);
      root.left = temp.left;
    }
    root.size = size(root.left) + size(root.right) + 1;
    return root;
  }

  public static void main(String[] args) {
    BST<String, Integer> st = new BST<String, Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++) {
      String key = StdIn.readString();
      st.put(key, i);
    }
    for (String s : st.keys())
      StdOut.println(s + " " + st.get(s));
  }
}
