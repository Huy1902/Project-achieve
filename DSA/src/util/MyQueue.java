package util;

import util.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyQueue<E> implements List<E> {
  transient private int size;

  transient private MyQueue.Node<E> first;
  transient private MyQueue.Node<E> last;

  static class Node<E> {
    E data;
    MyQueue.Node<E> next;

    Node(E data, MyQueue.Node<E> next) {
      this.next = next;
      this.data = data;
    }
  }

  private void unlinkFirst() throws NoSuchElementException {
    if (isEmpty()) {
      throw new NoSuchElementException("MyQueue underflow");
    }
    MyQueue.Node<E> current = first;
    first = first.next;
    current.next = null;
    --size;
  }

  private void linkLast(E data) {
    if (last == null) {
      first = new MyQueue.Node<>(data, null);
      last = first;
      ++size;
    } else {
      Node<E> newNode = new MyQueue.Node<>(data, last);
      last.next = newNode;
      last = newNode;
      ++size;
    }
  }


  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean add(E e) {
    linkLast(e);
    return true;
  }

  @Override
  public void clear() {

  }

  public boolean push(E data) {
    linkLast(data);
    return true;
  }

  public boolean pop() {
    try {
      unlinkFirst();
    } catch (NoSuchElementException e) {
      System.out.println("MyQueue is empty");
      return false;
    }
    return true;
  }

  public E first() {
    if (isEmpty()) {
      throw new NoSuchElementException("MyQueue is empty");
    }
    return first.data;
  }

  // the interator
  public Iterator<E> iterator() {
    return new MyQueueIterator(first);
  }

  private class MyQueueIterator implements Iterator<E> {
    private MyQueue.Node<E> current;

    public MyQueueIterator(MyQueue.Node<E> first) {
      current = first;
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      E data = current.data;
      current = current.next;
      return data;
    }
  }
}
