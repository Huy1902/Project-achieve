package threeoone.bigproject.util;

import java.util.Random;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * A service to generate random Strings.
 * This class is singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Component
public class RandomStringGenerator {
  private final String SPECIAL_CHARACTERS = "";
  private final String DIGITS = "0123456789";
  private final String LOWER = "qwertyuiopasdfghjklzxcvbnm";
  private final String UPPER = "QWERTYUIOPASDFGHJKLZXCVBNM";
  private final String CHARACTER_SET = SPECIAL_CHARACTERS + DIGITS + LOWER + UPPER;

  private final Random random = new Random();

  /**
   * Generate a random String with the given length.
   * The generated String uses this method's default character set,
   * which includes lowercase and uppercase alphabetical characters, digits, white space and special symbols which are url-safe.
   *
   * @param length the desired length of the generated String
   *
   * @return the generated String
   */
  public String generate(int length) {
    var result = new StringBuffer();

    for (int i = 0; i < length; i++) {
      int randCharIndex = random.nextInt(CHARACTER_SET.length());
      char randChar = CHARACTER_SET.charAt(randCharIndex);
      result.append(randChar);
    }

    return result.toString();
  }
}
