package Lec5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BubbleSortTest {

  @Test
  public void testSort_whenArrayIsNotSorted_thenArrayIsSorted() {
    //Arrange
    int[] input = {5, 3, 1, 4, 2};
    int[] expectedOutput = {1, 2, 3, 4, 5};

    //Act
    BubbleSort.sort(input);

    //Assert
    assertArrayEquals(expectedOutput, input);
  }

  @Test
  public void testSort_whenArrayIsAlreadySorted_thenArrayRemainsSorted() {
    //Arrange
    int[] input = {1, 2, 3, 4, 5};
    int[] expectedOutput = {1, 2, 3, 4, 5};

    //Act
    BubbleSort.sort(input);

    //Assert
    assertArrayEquals(expectedOutput, input);
  }

  @Test
  public void testSort_whenArrayContainsDuplicates_thenArrayIsSorted() {
    //Arrange
    int[] input = {2, 3, 2, 1, 1};
    int[] expectedOutput = {1, 1, 2, 2, 3};

    //Act
    BubbleSort.sort(input);

    //Assert
    assertArrayEquals(expectedOutput, input);
  }

  @Test
  public void testSort_whenArrayIsEmpty_thenRemainsEmpty() {
    //Arrange
    int[] input = {};
    int[] expectedOutput = {};

    //Act
    BubbleSort.sort(input);

    //Assert
    assertArrayEquals(expectedOutput, input);
  }
}