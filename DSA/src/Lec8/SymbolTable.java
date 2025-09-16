package Lec8;

import java.util.Iterator;
import java.util.TreeMap;

public class SymbolTable<Key extends Comparable<Key>, Value> implements Iterable<Key> {
  private TreeMap<Key, Value> symbolTable;

  public SymbolTable() {
    symbolTable = new TreeMap<>();
  }

  public Value get(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    return symbolTable.get(key);
  }

  public void put(Key key, Value value) {
    if(key == null) throw new IllegalArgumentException("null key");
    if(value == null) {
      symbolTable.remove(key);
    }
    symbolTable.put(key, value);
  }

  public void delete(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    symbolTable.remove(key);
  }

  public boolean contains(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    return symbolTable.containsKey(key);
  }

  public void remove(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    symbolTable.remove(key);
  }

  public boolean isEmpty() {
    return symbolTable.isEmpty();
  }

  public int size() {
    return symbolTable.size();
  }

  public Key min() {
    if(isEmpty()) {
      throw new IllegalArgumentException("empty symbol table");
    }
    return symbolTable.firstKey();
  }

  public Key max() {
    if(isEmpty()) {
      throw new IllegalArgumentException("empty symbol table");
    }
    return symbolTable.lastKey();
  }

  public Key floor(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    return symbolTable.floorKey(key);
  }

  public Key ceiling(Key key) {
    if(key == null) throw new IllegalArgumentException("null key");
    return symbolTable.ceilingKey(key);
  }

  public Iterable<Key> keys() {
    return null;
  }

  /**
   * Returns an iterator over elements of type {@code T}.
   *
   * @return an Iterator.
   */
  @Override
  public Iterator<Key> iterator() {
    return null;
  }
}
