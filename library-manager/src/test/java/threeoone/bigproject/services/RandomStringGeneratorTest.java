package threeoone.bigproject.services;

import org.junit.jupiter.api.Test;
import threeoone.bigproject.util.RandomStringGenerator;

class RandomStringGeneratorTest {
  RandomStringGenerator service = new RandomStringGenerator();

  @Test
  void test() {
    for (int i = 0; i < 5; i++) {
      System.out.println(service.generate(10));
    }
  }
}