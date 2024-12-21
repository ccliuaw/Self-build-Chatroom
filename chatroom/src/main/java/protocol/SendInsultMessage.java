package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a send insult message
 */
public class SendInsultMessage extends Message {

  private String senderUsername;
  private String recipientUsername;

  /**
   * Default constructor
   */
  public SendInsultMessage() {
  }

  /**
   * Construct a SendInsultMessage with given input arguments:
   *
   * @param senderUsername    the username for sender
   * @param recipientUsername the username for recipient
   */
  public SendInsultMessage(String senderUsername, String recipientUsername) {
    this.senderUsername = senderUsername;
    this.recipientUsername = recipientUsername;
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
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.SEND_INSULT;
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
  public String getRecipientUsername() {
    return recipientUsername;
  }

  @Override
  public String toString() {
    return "SendInsultMessage [senderUsername=" + senderUsername + ", recipientUsername="
        + recipientUsername + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendInsultMessage that = (SendInsultMessage) o;
    return Objects.equals(senderUsername, that.senderUsername) && Objects.equals(
        recipientUsername, that.recipientUsername);
  }

  @Override
  public int hashCode() {
    return Objects.hash(senderUsername, recipientUsername);
  }
}
