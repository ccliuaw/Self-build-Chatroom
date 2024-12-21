package protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a failed message
 */
public class FailedMessage extends Message {

  private String message;

  /**
   * Default constructor
   */
  public FailedMessage() {
  }

  /**
   * Construct a FailedMessage with given input arguments:
   *
   * @param message the message content
   */
  public FailedMessage(String message) {
    this.message = message;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue());

    byte[] messageBytes = (message != null ? message : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(messageBytes.length);
    out.write(messageBytes);
  }

  @Override
  public void decode(DataInputStream in) throws IOException {
    int messageSize = in.readInt();
    byte[] messageBytes = new byte[messageSize];
    in.readFully(messageBytes);
    this.message = new String(messageBytes, DEFAULT_CHAR_SET);
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.FAILED_MESSAGE;
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
  public String toString() {
    return "FailedMessage [message=" + message + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FailedMessage that = (FailedMessage) o;
    return Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(message);
  }
}
