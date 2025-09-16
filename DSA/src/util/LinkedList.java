package util;

public class LinkedList<E> implements List<E> {

  private int size = 0;

  private Node<E> first;

  private Node<E> last;


  static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
      this.item = element;
      this.next = next;
      this.prev = prev;
    }
  }

  Node<E> node(int index) {
    Node<E> x;
    if (index < (size >> 1)) {
      x = first;
      for (int i = 0; i < index; i++)
        x = x.next;
    } else {
      x = last;
      for (int i = size - 1; i > index; i--)
        x = x.prev;
    }
    return x;
  }

  public boolean isEmpty() {
    return size == 0;
  }


  void linkTail(E e) {
    Node<E> l = last;
    Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null) first = newNode;
    else l.next = newNode;
    size++;

  }

  void linkBefore(E e, Node<E> flag) {
    Node<E> back = flag.prev;
    Node<E> newNode = new Node<>(back, e, flag);
    flag.prev = newNode;
    if (back == null) first = newNode;
    else back.next = newNode;
    size++;

  }

  E unlink(Node<E> x) {
    E element = x.item;
    Node<E> next = x.next;
    Node<E> prev = x.prev;

    if (prev == null) {
      first = next;
    } else {
      prev.next = next;
      x.prev = null;
    }

    if (next == null) {
      last = prev;
    } else {
      next.prev = prev;
      x.next = null;
    }

    x.item = null;
    size--;

    return element;
  }

  public int size() {
    return size;
  }

  public boolean add(E e) {
    linkTail(e);
    return true;
  }

  public void clear() {
    for (Node<E> x = first; x != null; ) {
      Node<E> next = x.next;
      x.item = null;
      x.next = null;
      x.prev = null;
      x = next;
    }
    first = last = null;
    size = 0;

  }

  public E getHeadValue() {
    return first.item;
  }

  public E getTailValue() {
    return last.item;
  }

  public void add(int index, E element) {
    if (index == size) linkTail(element);
    else linkBefore(element, node(index));
  }

  public E remove(int index) {
    return unlink(node(index));
  }
}