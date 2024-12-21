package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a broadcast message
 */
public class BroadcastMessage extends Message {

  private String senderUsername;
  private String message;

  /**
   * Default constructor
   */
  public BroadcastMessage() {
  }

  /**
   * Construct a BroadcastMessage with given input arguments:
   *
   * @param senderUsername the username for sender
   * @param message        the message content
   */
  public BroadcastMessage(String senderUsername, String message) {
    this.senderUsername = senderUsername;
    this.message = message;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue());
    byte[] senderBytes = (senderUsername != null ? senderUsername : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(senderBytes.length);
    out.write(senderBytes);

    byte[] messageBytes = (message != null ? message : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(messageBytes.length);
    out.write(messageBytes);
  }

  @Override
  public void decode(DataInputStream in) throws IOException {
    int senderSize = in.readInt();
    byte[] senderBytes = new byte[senderSize];
    in.readFully(senderBytes);
    this.senderUsername = new String(senderBytes, DEFAULT_CHAR_SET);

    int messageSize = in.readInt();
    byte[] messageBytes = new byte[messageSize];
    in.readFully(messageBytes);
    this.message = new String(messageBytes, DEFAULT_CHAR_SET);
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.BROADCAST_MESSAGE;
  }

  /**
   * get the sender username
   *
   * @return sender username
   */
  public String getSenderUsername() {
    return senderUsername;
  }

  /**
   * get the message content
   *
   * @return message content
   */
  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BroadcastMessage that = (BroadcastMessage) o;
    return Objects.equals(senderUsername, that.senderUsername) && Objects.equals(
        message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(senderUsername, message);
  }

  @Override
  public String toString() {
    return "BroadcastMessage [senderUsername=" + senderUsername + ", message=" + message + "]";
  }
}
