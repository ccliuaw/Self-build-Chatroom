package client;

/**
 * Main class for the ChatClient to start
 */
public class ChatLauncher {

  /**
   * main method for start the ChatClient
   *
   * @param args default empty
   */
  public static void main(String[] args) {
    new ChatClient(System.in).start();
  }
}
