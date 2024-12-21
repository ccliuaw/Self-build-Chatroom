package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a direct message
 */
public class DirectMessage extends Message {

  private String senderUsername;
  private String recipientUsername;
  private String message;

  /**
   * Default constructor
   */
  public DirectMessage() {
  }

  /**
   * Construct a Direct Message with given input arguments:
   *
   * @param senderUsername    the username for sender
   * @param recipientUsername the username for recipient
   * @param message           the message content
   */
  public DirectMessage(String senderUsername, String recipientUsername, String message) {
    this.senderUsername = senderUsername;
    this.recipientUsername = recipientUsername;
    this.message = message;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue());

    byte[] senderBytes = (senderUsername != null ? senderUsername : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(senderBytes.length);
    out.write(senderBytes);

    byte[] recipientBytes = (recipientUsername != null ? recipientUsername : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(recipientBytes.length);
    out.write(recipientBytes);

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

    int recipientSize = in.readInt();
    byte[] recipientBytes = new byte[recipientSize];
    in.readFully(recipientBytes);
    this.recipientUsername = new String(recipientBytes, DEFAULT_CHAR_SET);

    int messageSize = in.readInt();
    byte[] messageBytes = new byte[messageSize];
    in.readFully(messageBytes);
    this.message = new String(messageBytes, DEFAULT_CHAR_SET);
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.DIRECT_MESSAGE;
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
   * get the recipient username
   *
   * @return recipient username
   */
  public String getRecipientUsername() {
    return recipientUsername;
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
    return "Direct message [from: " + senderUsername + ", to: " + recipientUsername + ", message: "
        + message + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DirectMessage that = (DirectMessage) o;
    return Objects.equals(senderUsername, that.senderUsername) && Objects.equals(
        recipientUsername, that.recipientUsername) && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(senderUsername, recipientUsername, message);
  }
}
