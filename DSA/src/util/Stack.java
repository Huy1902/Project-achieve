package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<E> implements List<E> {
  transient private int size;

  transient private Node<E> first;

  static class Node<E> {
    E data;
    Node<E> next;
    Node(E data, Node<E> next) {
      this.next = next;
      this.data = data;
    }
  }
  private void unlinkFirst() throws NoSuchElementException {
    if(isEmpty()) {
      throw new NoSuchElementException("Stack underflow");
    }
    Node<E> current = first;
    first = first.next;
    current.next = null;
    --size;
  }
  private void linkFirst(E data) {
    if(first == null) {
      first = new Node<>(data, null);
      ++size;
    } else {
      first = new Node<>(data, first);
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
    linkFirst(e);
    return true;
  }

  @Override
  public void clear() {

  }

  public boolean push(E data) {
    linkFirst(data);
    return true;
  }

  public boolean pop() {
    try {
      unlinkFirst();
    } catch (NoSuchElementException e) {
      System.out.println("Stack is empty");
      return false;
    }
    return true;
  }

  public E top() {
    if(isEmpty()) {
      throw new NoSuchElementException("Stack is empty");
    }
    return first.data;
  }

  // the interator
  public Iterator<E> iterator() {
    return new StackIterator(first);
  }

  private class StackIterator implements Iterator<E> {
    private Node<E> current;

    public StackIterator(Node<E> first) {
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
