package protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum class for the message types
 */
public enum MessageType {
  /**
   * for connect message
   */
  CONNECT_MESSAGE(Message.CONNECT_MESSAGE_CODE),
  /**
   * for connect response
   */
  CONNECT_RESPONSE(Message.CONNECT_RESPONSE_CODE),
  /**
   * for disconnect message
   */
  DISCONNECT_MESSAGE(Message.DISCONNECT_MESSAGE_CODE),
  /**
   * for query connected users message
   */
  QUERY_CONNECTED_USERS(Message.QUERY_CONNECTED_USERS_CODE),
  /**
   * for query user response
   */
  QUERY_USER_RESPONSE(Message.QUERY_USER_RESPONSE_CODE),
  /**
   * for broadcast message
   */
  BROADCAST_MESSAGE(Message.BROADCAST_MESSAGE_CODE),
  /**
   * for direct message
   */
  DIRECT_MESSAGE(Message.DIRECT_MESSAGE_CODE),
  /**
   * failed message
   */
  FAILED_MESSAGE(Message.FAILED_MESSAGE_CODE),
  /**
   * send insult
   */
  SEND_INSULT(Message.SEND_INSULT_CODE);

  private final int value;

  /**
   * pairing the message type
   *
   * @param value desired value for the message type
   */
  MessageType(int value) {
    this.value = value;
  }

  /**
   * get the values for the message type
   *
   * @return the value of the type
   */
  public int getValue() {
    return value;
  }

  // Reverse mapping for integer to enum lookup
  private static final Map<Integer, MessageType> BY_VALUE = new HashMap<>();

  static {
    for (MessageType type : values()) {
      BY_VALUE.put(type.value, type);
    }
  }

  /**
   * generate message type from value
   *
   * @param value input value
   * @return message type
   */
  public static MessageType fromValue(int value) {
    return BY_VALUE.get(value);
  }
}
