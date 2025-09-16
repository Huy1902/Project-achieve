package util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StackTest {
  private Stack<Integer> stack;
  @Before
  public void setUp() {
    stack = new Stack<>();
  }

  @Test
  public void testAdd() {
    assertTrue(stack.add(2));
  }
  @Test
  public void testSize() {
    assertEquals(0, stack.size());
  }
  @Test
  public void testIsEmpty() {
    assertTrue(stack.isEmpty());
  }
  @Test
  public void testClear() {
    stack.clear();
    assertTrue(stack.isEmpty());
  }
  @Test
  public void testPop() {
    stack.add(2);
    assertTrue(stack.pop());
    assertTrue(stack.isEmpty());
  }
  @Test
  public void testPush() {
    stack.push(5);
    assertTrue(stack.push(7));
    assertEquals(2, stack.size());
    assertEquals(7, (int)stack.top());
  }
  @Test
  public void testTop() {
    stack.add(2);
    stack.add(3);
    assertEquals(3, (int)stack.top());
  }
  @Test
  public void testIterator() {
    stack.push(1);
    stack.push(2);
    stack.push(3);
    Iterator<Integer> ite = stack.iterator();
    ArrayList<Integer> output = new ArrayList<>();
    ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(3, 2, 1));
    while(ite.hasNext()) {
      output.add(ite.next());
    }
    assertEquals(expected, output);
  }

}
