package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Chat ui test.
 */
class ChatUITest {

  private ChatUI chatUI;
  private ByteArrayOutputStream outStream;
  private ByteArrayOutputStream errStream;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    outStream = new ByteArrayOutputStream();
    errStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));
    System.setErr(new PrintStream(errStream));
  }

  /**
   * Test get host from user.
   */
  @Test
  void testGetHostFromUser() {
    String input = "testhost\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    chatUI = new ChatUI(System.in);

    assertEquals("testhost", chatUI.getHostFromUser());
    assertTrue(outStream.toString().contains("Enter IP address of server to connect to"));
  }

  /**
   * Test get host default.
   */
  @Test
  void testGetHostDefault() {
    String input = "\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    chatUI = new ChatUI(System.in);

    assertEquals("localhost", chatUI.getHostFromUser());
  }

  /**
   * Test get port from user.
   */
  @Test
  void testGetPortFromUser() {
    String input = "8080\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    chatUI = new ChatUI(System.in);

    assertEquals("8080", chatUI.getPortFromUser());
    assertTrue(outStream.toString().contains("Enter Server Port"));
  }

  /**
   * Test get port default.
   */
  @Test
  void testGetPortDefault() {
    String input = "\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    chatUI = new ChatUI(System.in);

    assertEquals("12345", chatUI.getPortFromUser());
  }

  /**
   * Test login.
   */
  @Test
  void testLogin() {
    String input = "testuser\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    chatUI = new ChatUI(System.in);

    assertEquals("testuser", chatUI.login());
    assertTrue(outStream.toString().contains("Enter username"));
  }

  /**
   * Test get chat box input.
   */
  @Test
  void testGetChatBoxInput() {
    String input = "Hello\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    chatUI = new ChatUI(System.in);

    assertEquals("Hello", chatUI.getChatBoxInput("testuser"));
    assertTrue(outStream.toString().contains("testuser: >"));
  }

  /**
   * Test display welcome to chat message.
   */
  @Test
  void testDisplayWelcomeToChatMessage() {
    chatUI = new ChatUI(System.in);
    chatUI.displayWelcomeToChatMessage();
    String output = outStream.toString();
    assertTrue(output.contains("Connected to the chat!"));
    assertTrue(output.contains("Enter messages or type \"?\" for help."));
  }

  /**
   * Test show received message.
   */
  @Test
  void testShowReceivedMessage() {
    chatUI = new ChatUI(System.in);
    chatUI.showReceivedMessage("user1", "Hello", "user2");
    assertTrue(outStream.toString().contains("(private) user1: Hello"));

    chatUI.showReceivedMessage("user1", "Hello");
    assertTrue(outStream.toString().contains("(broadcast) user1: Hello"));
  }

  /**
   * Test show server message.
   */
  @Test
  void testShowServerMessage() {
    chatUI = new ChatUI(System.in);
    chatUI.showServerMessage("Test message");
    assertTrue(outStream.toString().contains("SERVER: Test message"));
  }

  /**
   * Test show server error.
   */
  @Test
  void testShowServerError() {
    chatUI = new ChatUI(System.in);
    chatUI.showServerError("Test error");
    assertTrue(errStream.toString().contains("SERVER ERROR: Test error"));
  }

  /**
   * Test show broadcast message.
   */
  @Test
  void testShowBroadcastMessage() {
    chatUI = new ChatUI(System.in);
    BroadcastMessage message = new BroadcastMessage("user1", "Hello everyone");
    chatUI.showBroadcastMessage(message);
    assertTrue(outStream.toString().contains("(broadcast) user1: Hello everyone"));
  }

  /**
   * Test show direct message.
   */
  @Test
  void testShowDirectMessage() {
    chatUI = new ChatUI(System.in);
    DirectMessage message = new DirectMessage("user1", "user2", "Hello");
    chatUI.showDirectMessage(message);
    assertTrue(outStream.toString().contains("(private) user1: Hello"));
  }

  /**
   * Test show failed message.
   */
  @Test
  void testShowFailedMessage() {
    chatUI = new ChatUI(System.in);
    FailedMessage message = new FailedMessage("Error occurred");
    chatUI.showFailedMessage(message);
    assertTrue(errStream.toString().contains("SERVER ERROR: Error occurred"));
  }

  /**
   * Test show query users response.
   */
  @Test
  void testShowQueryUsersResponse() {
    chatUI = new ChatUI(System.in);
    QueryUsersResponse response = new QueryUsersResponse(Arrays.asList("user1", "user2", "user3"));
    chatUI.showQueryUsersResponse(response);
    assertTrue(outStream.toString().contains("Connected Users: user1, user2, user3"));

    QueryUsersResponse emptyResponse = new QueryUsersResponse(Arrays.asList());
    chatUI.showQueryUsersResponse(emptyResponse);
    assertTrue(outStream.toString().contains("No other connected users"));
  }

  /**
   * Test show disconnect response.
   */
  @Test
  void testShowDisconnectResponse() {
    chatUI = new ChatUI(System.in);
    ConnectResponse response = new ConnectResponse(true, "Disconnected successfully");
    chatUI.showDisconnectResponse(response);
    assertTrue(outStream.toString().contains("SERVER: Disconnected successfully"));
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    ChatUI ui1 = new ChatUI(System.in);
    ChatUI ui2 = new ChatUI(System.in);
    ChatUI ui3 = new ChatUI(new ByteArrayInputStream("test".getBytes()));

    assertEquals(ui1, ui2);
    assertNotEquals(ui1, ui3);
    assertNotEquals(ui1, null);
    assertNotEquals(ui1, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    ChatUI ui1 = new ChatUI(System.in);
    ChatUI ui2 = new ChatUI(System.in);
    ChatUI ui3 = new ChatUI(new ByteArrayInputStream("test".getBytes()));

    assertEquals(ui1.hashCode(), ui2.hashCode());
    assertNotEquals(ui1.hashCode(), ui3.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ChatUI ui = new ChatUI(System.in);
    String toString = ui.toString();

    assertTrue(toString.contains("ChatUI"));
    assertTrue(toString.contains("outStream"));
    assertTrue(toString.contains("errorStream"));
    assertTrue(toString.contains("inputStream"));
  }
}