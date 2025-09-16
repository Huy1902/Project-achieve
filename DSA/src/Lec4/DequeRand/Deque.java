package Lec4.DequeRand;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

  private Node first;
  private Node last;
  private int size;

  // construct an empty deque
  public Deque() {
  }

  // is the deque empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the deque
  public int size() {
    return size;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) throw new IllegalArgumentException();
    if (isEmpty()) {
      last = new Node();
      last.item = item;
      first = last;
    } else {
      Node oldFirst = first;
      first = new Node();
      first.item = item;
      first.next = oldFirst;
      oldFirst.previous = first;
    }
    ++size;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) throw new IllegalArgumentException();
    if(isEmpty()) {
      first = new Node();
      first.item = item;
      last = first;
    } else {
      Node oldLast = last;
      last = new Node();
      last.item = item;
      last.previous = oldLast;
      oldLast.next = last;
    }
    ++size;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    Item item = first.item;
    size--;
    if(isEmpty()) {
      first = null;
      last = null;
    }
    else {
      first = first.next;
      first.previous = null;
    }
    return item;
  }

  // remove and return the item from the back
  public Item removeLast() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    Item item = last.item;
    size--;
    if(isEmpty()) {
      first = null;
      last = null;
    }
    else {
      last = last.previous;
      last.next = null;
    }
    return item;
  }

  // return an iterator over items in order from front to back
  @Override
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  private class Node {
    private Item item;
    private Node next;
    private Node previous;
  }

  private class DequeIterator implements Iterator<Item> {
    private Node current = first;

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Item item = current.item;
      current = current.next;
      return item;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    Deque<Integer> dq = new Deque<>();
    dq.addFirst(1);
    dq.addFirst(2);
    dq.addLast(3);
    dq.addLast(4);
    dq.addFirst(5);
    dq.removeFirst();
    dq.removeLast();

    Iterator<Integer> it = dq.iterator();
    while(it.hasNext()) {
      System.out.println(it.next());
    }
  }

}