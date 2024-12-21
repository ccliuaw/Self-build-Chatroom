package client;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Server message handler test.
 */
class ServerMessageHandlerTest {

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    ChatUI chatUI1 = new ChatUI(System.in);
    ChatUI chatUI2 = new ChatUI(new ByteArrayInputStream("test".getBytes()));

    ServerMessageHandler handler1 = new ServerMessageHandler(chatUI1);
    ServerMessageHandler handler2 = new ServerMessageHandler(chatUI1);
    ServerMessageHandler handler3 = new ServerMessageHandler(chatUI2);

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
    ChatUI chatUI1 = new ChatUI(System.in);
    ChatUI chatUI2 = new ChatUI(new ByteArrayInputStream("test".getBytes()));

    ServerMessageHandler handler1 = new ServerMessageHandler(chatUI1);
    ServerMessageHandler handler2 = new ServerMessageHandler(chatUI1);
    ServerMessageHandler handler3 = new ServerMessageHandler(chatUI2);

    assertEquals(handler1.hashCode(), handler2.hashCode());
    assertNotEquals(handler1.hashCode(), handler3.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ChatUI chatUI = new ChatUI(System.in);
    ServerMessageHandler handler = new ServerMessageHandler(chatUI);
    String toString = handler.toString();

    assertTrue(toString.contains("ServerMessageHandler"));
    assertTrue(toString.contains("chatUI"));
  }
}