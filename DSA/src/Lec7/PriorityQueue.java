package Lec7;

import java.util.Comparator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PriorityQueue<T> {
  private int size;
  private T[] queue;
  private Comparator<T> comparator;

  public PriorityQueue(int capacity) {
    queue = (T[]) new Object[capacity];
    size = 0;
  }

  public PriorityQueue() {
    this(1);
  }

  public PriorityQueue(int capacity, Comparator<T> comparator) {
    this.comparator = comparator;
    queue = (T[]) new Object[capacity];
    size = 0;
  }

  public PriorityQueue(Comparator<T> comparator) {
    this(1, comparator);
  }

  public PriorityQueue(T[] array) {
    size = array.length;
    queue = (T[]) new Object[size + 1];
    for (int i = 0; i < size; ++i) {
      queue[i + 1] = array[i];
    }
    for (int k = size / 2; k >= 1; --k) {
      sink(k);
    }
  }

  private boolean less(int i, int j) {
    if (comparator == null) {
      return ((Comparable<T>) queue[i]).compareTo(queue[j]) < 0;
    } else {
      return comparator.compare(queue[i], queue[j]) < 0;
    }
  }

  private void exchange(int i, int j) {
    T temp = queue[i];
    queue[i] = queue[j];
    queue[j] = temp;
  }

  private void sink(int k) {
    while (2 * k <= size) {
      int j = 2 * k;
      // Get max value in two child
      if (j < size && less(j, j + 1)) {
        ++j;
      }
      if (!less(k, j)) {
        break;
      }
      exchange(k, j);
      k = j;
    }
  }

  private void swim(int k) {
    while (k > 1 && less(k / 2, k)) {
      exchange(k / 2, k);
      k /= 2;
    }
  }

  private void resize(int capacity) {
    T[] temp = (T[]) new Object[capacity];
    for (int i = 1; i <= size; ++i) {
      temp[i] = queue[i];
    }
    queue = temp;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public void insert(T item) {
    if (size == queue.length - 1) {
      resize(2 * queue.length);
    }
    StdOut.print(item);
    queue[++size] = item;
    swim(size);
  }

  public T deleteMax() {
    if (isEmpty()) {
      throw new RuntimeException("Priority queue underflow");
    }
    T max = queue[1];
    exchange(1, size--);
    sink(1);
    queue[size + 1] = null;
    if (size > 0 && size == (queue.length - 1) / 4) {
      resize(queue.length / 2);
    }
    return max;
  }

  public int size() {
    return size;
  }

  public static void main(String[] args) {
    PriorityQueue<String> pq = new PriorityQueue<String>();
    while (!StdIn.isEmpty()) {
      String item = StdIn.readString();
      if (!item.equals("-")) {
        StdOut.print(item + "\n");
        pq.insert(item);
      }
      else if (!pq.isEmpty()) StdOut.print(pq.deleteMax() + " ");
    }
    StdOut.println("(" + pq.size() + " left on pq)");
  }
}
