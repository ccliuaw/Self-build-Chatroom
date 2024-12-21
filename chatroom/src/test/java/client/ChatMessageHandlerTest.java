package client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Chat message handler test.
 */
class ChatMessageHandlerTest {

  private ChatMessageHandler handler;
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errContent;
  private DataOutputStream dataOut;
  private ChatUI chatUI;
  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    dataOut = new DataOutputStream(outContent);
    handler = new ChatMessageHandler(dataOut, new ChatUI(System.in));
  }

  /**
   * Test handle chat input help.
   *
   * @throws IOException the io exception
   */
  @Test
  void testHandleChatInputHelp() throws IOException {
    handler.handleChatInput("?");
    assertTrue(outContent.toString().contains("Help instructions"));
  }

  /**
   * Test handle chat input logoff.
   */
  @Test
  void testHandleChatInputLogoff() {
    handler.handleChatInput("logoff");
    try {
      DataInputStream dataInputStream = new DataInputStream(
          new ByteArrayInputStream(outContent.toByteArray()));
      assertInstanceOf(DisconnectMessage.class, Message.decodeFromStream(dataInputStream));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test handle chat input who.
   */
  @Test
  void testHandleChatInputWho() {
    handler.handleChatInput("who");
    try {
      DataInputStream dataInputStream = new DataInputStream(
          new ByteArrayInputStream(outContent.toByteArray()));
      assertInstanceOf(QueryUsersMessage.class, Message.decodeFromStream(dataInputStream));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test handle chat input insult.
   */
  @Test
  void testHandleChatInputInsult() {
    handler.handleChatInput("!user");
    try {
      DataInputStream dataInputStream = new DataInputStream(
          new ByteArrayInputStream(outContent.toByteArray()));
      assertInstanceOf(SendInsultMessage.class, Message.decodeFromStream(dataInputStream));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test handle chat input broadcast.
   *
   * @throws IOException the io exception
   */
  @Test
  void testHandleChatInputBroadcast() throws IOException {
    handler.handleChatInput("@all hello");
    try {
      DataInputStream dataInputStream = new DataInputStream(
          new ByteArrayInputStream(outContent.toByteArray()));
      assertInstanceOf(BroadcastMessage.class, Message.decodeFromStream(dataInputStream));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test handle chat input direct message.
   *
   * @throws IOException the io exception
   */
  @Test
  void testHandleChatInputDirectMessage() throws IOException {
    handler.handleChatInput("@user Hello");
    try {
      DataInputStream dataInputStream = new DataInputStream(
          new ByteArrayInputStream(outContent.toByteArray()));
      assertInstanceOf(DirectMessage.class, Message.decodeFromStream(dataInputStream));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test handle chat input unknown command.
   */
  @Test
  void testHandleChatInputUnknownCommand() {
    handler.handleChatInput("unknown");
    assertTrue(errContent.toString().contains("Unknown command"));
  }

  /**
   * Test connect to chat.
   *
   * @throws IOException the io exception
   */
  @Test
  void testConnectToChat() throws IOException {
    handler.connectToChat("testUser");
    try {
      DataInputStream dataInputStream = new DataInputStream(
          new ByteArrayInputStream(outContent.toByteArray()));
      assertInstanceOf(ConnectMessage.class, Message.decodeFromStream(dataInputStream));
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
    DataOutputStream out1 = new DataOutputStream(baos1);
    DataOutputStream out2 = new DataOutputStream(baos2);
    ChatUI chatUI1 = new ChatUI(System.in);
    ChatUI chatUI2 = new ChatUI(System.in);

    ChatMessageHandler handler1 = new ChatMessageHandler(out1, chatUI1);
    ChatMessageHandler handler2 = new ChatMessageHandler(out1, chatUI1);
    ChatMessageHandler handler3 = new ChatMessageHandler(out2, chatUI2);

    assertEquals(handler1, handler2);
    assertNotEquals(handler1, handler3);
    assertNotEquals(handler1, null);
    assertNotEquals(handler1, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
    DataOutputStream out1 = new DataOutputStream(baos1);
    DataOutputStream out2 = new DataOutputStream(baos2);
    ChatUI chatUI1 = new ChatUI(System.in);
    ChatUI chatUI2 = new ChatUI(System.in);

    ChatMessageHandler handler1 = new ChatMessageHandler(out1, chatUI1);
    ChatMessageHandler handler2 = new ChatMessageHandler(out1, chatUI1);
    ChatMessageHandler handler3 = new ChatMessageHandler(out2, chatUI2);

    assertEquals(handler1.hashCode(), handler2.hashCode());
    assertNotEquals(handler1.hashCode(), handler3.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(baos);
    ChatUI chatUI = new ChatUI(System.in);

    ChatMessageHandler handler = new ChatMessageHandler(out, chatUI);
    String toString = handler.toString();

    assertTrue(toString.contains("ChatMessageHandler"));
    assertTrue(toString.contains("out"));
    assertTrue(toString.contains("chatUI"));
    assertTrue(toString.contains("username"));
  }

  /**
   * Restore streams.
   */
// Restore the original System.out and System.in
  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
    System.setIn(originalIn);
  }
}