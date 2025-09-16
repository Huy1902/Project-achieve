package util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTest {
  private LinkedList<Integer> test;

  @Before
  public void setUp() {
    test = new LinkedList<>();
  }

  @Test
  public void testIsEmpty() {
    assertTrue(test.isEmpty());
  }

  @Test
  public void testGetHead() {
    test.add(5);
    assertEquals(5, test.getHeadValue());
  }

  @Test
  public void testGetTail() {
    test.add(5);
    assertEquals(5, test.getTailValue());
  }

  @Test
  public void testAdd() {
    assertTrue(test.add(6));
    assertEquals(6, test.getTailValue());
  }

  @Test
  public void testSize() {
    test.add(5);
    assertEquals(1, test.size());
  }

  @Test
  public void testRemove() {
    test.add(5);
    test.add(6);
    test.add(7);
    assertEquals(6, test.remove(1));
    assertEquals(5, test.getHeadValue());
    assertEquals(7, test.getTailValue());
  }

  @Test
  public void testClear() {
    test.add(5);
    test.add(6);
    test.add(7);
    test.clear();
    assertTrue(test.isEmpty());
  }

  @Test
  public void testAddIndex() {
    test.add(5);
    test.add(6);
    test.add(7);
    test.add(1, 100);
    assertEquals(5, test.remove(0));
    assertEquals(100, test.getHeadValue());
  }


}