package server;

import java.util.Random;

/**
 * A class that randomly generator an insult sentence
 */
public class InsultGenerator {

  private static final String[] INSULTS = {
      "You're like a cloud... always disappearing when I need shade.",
      "You bring everyone so much joy... when you leave the room.",
      "Your secrets are safe with me. I never even listen when you tell me them.",
      "You're proof that even mistakes can be endearing.",
      "You're as useful as a screen door on a submarine.",
      "You're like a broken pencil... pointless.",
      "Your jokes make crickets embarrassed to chirp.",
      "Your brain cells must have gone on vacation... permanently.",
      "You're the human equivalent of a participation trophy.",
      "You have something on your chin... no, the third one down."
  };

  /**
   * generate an insult sentence
   *
   * @return an insult sentence
   */
  public static String generateInsult() {
    Random random = new Random();
    int index = random.nextInt(INSULTS.length);
    return INSULTS[index];
  }
}

