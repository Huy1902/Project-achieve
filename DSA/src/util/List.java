package util;

import java.util.Iterator;

public interface List<E> {
  int size();

  boolean isEmpty();

  boolean add(E e);

  void clear();
}