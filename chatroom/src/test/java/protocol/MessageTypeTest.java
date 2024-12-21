package protocol;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Message type test.
 */
class MessageTypeTest {

  /**
   * Gets value.
   */
  @Test
  void getValue() {
    assertEquals(19, MessageType.CONNECT_MESSAGE.getValue());
    assertEquals(20, MessageType.CONNECT_RESPONSE.getValue());
    assertEquals(21, MessageType.DISCONNECT_MESSAGE.getValue());
    assertEquals(22, MessageType.QUERY_CONNECTED_USERS.getValue());
    assertEquals(23, MessageType.QUERY_USER_RESPONSE.getValue());
    assertEquals(24, MessageType.BROADCAST_MESSAGE.getValue());
    assertEquals(25, MessageType.DIRECT_MESSAGE.getValue());
    assertEquals(26, MessageType.FAILED_MESSAGE.getValue());
    assertEquals(27, MessageType.SEND_INSULT.getValue());
  }

  /**
   * From value.
   */
  @Test
  void fromValue() {
    assertEquals(MessageType.CONNECT_MESSAGE, MessageType.fromValue(19));
    assertEquals(MessageType.CONNECT_RESPONSE, MessageType.fromValue(20));
    assertEquals(MessageType.DISCONNECT_MESSAGE, MessageType.fromValue(21));
    assertEquals(MessageType.QUERY_CONNECTED_USERS, MessageType.fromValue(22));
    assertEquals(MessageType.QUERY_USER_RESPONSE, MessageType.fromValue(23));
    assertEquals(MessageType.BROADCAST_MESSAGE, MessageType.fromValue(24));
    assertEquals(MessageType.DIRECT_MESSAGE, MessageType.fromValue(25));
    assertEquals(MessageType.FAILED_MESSAGE, MessageType.fromValue(26));
    assertEquals(MessageType.SEND_INSULT, MessageType.fromValue(27));
  }

  /**
   * Invalid value test.
   */
  @Test
  void invalidValueTest() {
    assertNull(MessageType.fromValue(0));
    assertNull(MessageType.fromValue(18));
    assertNull(MessageType.fromValue(28));
    assertNull(MessageType.fromValue(-1));
  }

  /**
   * Map test.
   */
  @Test
  void mapTest() {
    for (MessageType type : MessageType.values()) {
      assertEquals(type, MessageType.fromValue(type.getValue()));
    }
  }
}