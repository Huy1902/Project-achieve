package Lec4.DequeRand;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] items;
  private int size;
  // construct an empty randomized queue
  public RandomizedQueue() {
    items = (Item[]) new Object[2];
    size = 0;
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return size;
  }

  // add the item
  public void enqueue(Item item) {
    if(item == null) throw new IllegalArgumentException();
    if(size == items.length) resize(2 * items.length);
    if(size == 0) {
      items[size++] = item;
      return;
    }
    int random = StdRandom.uniformInt(size);
    Item temp = items[random];
    items[random] = item;
    items[size++] = temp;
  }

  // remove and return a random item
  public Item dequeue() {
    if(isEmpty()) throw new NoSuchElementException();
    if(size > 0 && size == items.length / 4) resize(items.length / 2);
    int random = StdRandom.uniformInt(size);
    Item item = items[random];
    items[random] = items[--size];
    items[size] = null;
    return item;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    if(isEmpty()) throw new NoSuchElementException();
    return items[StdRandom.uniformInt(size)];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private int current = 0;
    private int[] randomIndices = new int[size];
    public RandomizedQueueIterator() {
      for(int i = 0; i < size; i++) {
        randomIndices[i] = i;
      }
      StdRandom.shuffle(randomIndices);
    }

    @Override
    public boolean hasNext() {
      return current < size;
    }

    @Override
    public Item next() {
      if(!hasNext()) throw new NoSuchElementException();
      return items[randomIndices[current++]];
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private void resize(int capacity) {
    Item[] temp = (Item[]) new Object[capacity];
    for(int i = 0; i < size; i++) {
      temp[i] = items[i];
    }
    items = temp;
  }


  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<Integer> rq = new RandomizedQueue<>();
    for(int i = 0; i < 10; i++) {
      rq.enqueue(i);
    }
    StdOut.println("---------------------");
    for(int i = 0; i < 5; i++) {
      StdOut.println(rq.dequeue());
    }
    StdOut.println("---------------------");
    for(int i = 0; i < 10; i++) {
      StdOut.println(rq.sample());
    }
    StdOut.println("---------------------");

    Iterator<Integer> it = rq.iterator();
    while(it.hasNext()) {
      StdOut.println(it.next());
    }
  }

}