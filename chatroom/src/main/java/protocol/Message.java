package protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Abstract class for the Message
 */
public abstract class Message {

  /**
   * Represents an empty string.
   */
  public static final String EMPTY_STRING = "";
  /**
   * Error message for unknown message types.
   */
  public static final String UNKNOWN_MESSAGE_ERROR = "Unknown message";
  /**
   * Message identifier for connect message.
   */
  public static final int CONNECT_MESSAGE_CODE = 19;
  /**
   * Message identifier for connect response.
   */
  public static final int CONNECT_RESPONSE_CODE = 20;
  /**
   * Message identifier for disconnect message.
   */
  public static final int DISCONNECT_MESSAGE_CODE = 21;
  /**
   * Message identifier for query connected users message.
   */
  public static final int QUERY_CONNECTED_USERS_CODE = 22;
  /**
   * Message identifier for query user response.
   */
  public static final int QUERY_USER_RESPONSE_CODE = 23;
  /**
   * Message identifier for broadcast message.
   */
  public static final int BROADCAST_MESSAGE_CODE = 24;
  /**
   * Message identifier for direct message.
   */
  public static final int DIRECT_MESSAGE_CODE = 25;
  /**
   * Message identifier for failed message.
   */
  public static final int FAILED_MESSAGE_CODE = 26;
  /**
   * Message identifier for send insult message.
   */
  public static final int SEND_INSULT_CODE = 27;
  /**
   * Default character set for string encoding/decoding.
   */
  public static final Charset DEFAULT_CHAR_SET = StandardCharsets.UTF_8;

  /**
   * Encode the message by following the protocol
   *
   * @param out the DataOutputStream
   * @throws IOException for exception
   */
  public abstract void encode(DataOutputStream out) throws IOException;

  /**
   * Decode the message by following the protocol
   *
   * @param in the DataInputStream
   * @throws IOException for exception
   */
  public abstract void decode(DataInputStream in) throws IOException;

  /**
   * get the message type by the protocol
   *
   * @return the message type
   */
  public abstract MessageType getMessageType();

  // Factory method to create a message based on type
  private static Message create(int messageTypeValue) throws IOException {
    MessageType messageType = MessageType.fromValue(messageTypeValue);
    if (messageType == null) {
      throw new IOException(UNKNOWN_MESSAGE_ERROR);
    }

    return switch (messageType) {
      case CONNECT_MESSAGE -> new ConnectMessage();
      case CONNECT_RESPONSE -> new ConnectResponse();
      case DISCONNECT_MESSAGE -> new DisconnectMessage();
      case QUERY_CONNECTED_USERS -> new QueryUsersMessage();
      case QUERY_USER_RESPONSE -> new QueryUsersResponse();
      case BROADCAST_MESSAGE -> new BroadcastMessage();
      case DIRECT_MESSAGE -> new DirectMessage();
      case FAILED_MESSAGE -> new FailedMessage();
      case SEND_INSULT -> new SendInsultMessage();
      default -> throw new IOException(UNKNOWN_MESSAGE_ERROR);
    };
  }

  /**
   * Decoding a message from a byte array
   *
   * @param in DataInputStream
   * @return Message instance
   * @throws IOException for exceptions
   */
  public static Message decodeFromStream(DataInputStream in) throws IOException {
    int messageTypeValue = in.readInt(); // Read the message type integer
    Message message = Message.create(messageTypeValue);
    message.decode(in);
    return message;
  }

  /**
   * Encoding a message into a byte array
   *
   * @return byte[] form of message
   * @throws IOException for exceptions
   */
  public byte[] encode() throws IOException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArrayOutputStream)) {

      encode(out); // Let the specific message handle its own encoding
      out.flush();
      return byteArrayOutputStream.toByteArray();
    }
  }

  /**
   * send the message to stream
   *
   * @param out DataOutputStream
   * @throws IOException for exceptions
   */
  public void sendToStream(DataOutputStream out) throws IOException {
    encode(out);
    out.flush();
  }
}
