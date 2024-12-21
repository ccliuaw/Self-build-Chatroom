package server;

import java.io.IOException;

/**
 * The type Server launcher.
 */
public class ServerLauncher {

  /**
   * Server Error Message Prefix
   */
  public static final String SERVER_ERROR_PREFIX = "Server error: ";

  /**
   * main method for start the ChatClient
   *
   * @param args default empty
   */
  public static void main(String[] args) {
    try {
      new Server().start();
    } catch (IOException e) {
      System.out.println(SERVER_ERROR_PREFIX + e.getMessage());
    }
  }
}
