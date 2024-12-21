package server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Insult generator test.
 */
class InsultGeneratorTest {

  /**
   * Generate insult.
   */
  @Test
  void generateInsult() {
    String i1 = InsultGenerator.generateInsult();
    String i2 = InsultGenerator.generateInsult();
    System.out.println(i1);
    System.out.println(i2);
  }

  /**
   * Test generate insult not null or empty.
   */
  @Test
  public void testGenerateInsultNotNullOrEmpty() {
    // Test that the generated insult is not null or empty
    String insult = InsultGenerator.generateInsult();
    assertNotNull(insult, "Insult should not be null");
    assertFalse(insult.isEmpty(), "Insult should not be empty");
  }

}