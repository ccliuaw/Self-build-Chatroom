package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a disconnect message
 */
public class DisconnectMessage extends Message {

  private String username;

  /**
   * Default constructor
   */
  public DisconnectMessage() {
  }

  /**
   * Construct a DisconnectMessage with given input arguments:
   *
   * @param username the username for sender
   */
  public DisconnectMessage(String username) {
    this.username = username;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue());
    byte[] usernameBytes = (username != null ? username : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(usernameBytes.length);
    out.write(usernameBytes);
  }

  @Override
  public void decode(DataInputStream in) throws IOException {
    int usernameSize = in.readInt();
    byte[] usernameBytes = new byte[usernameSize];
    in.readFully(usernameBytes);
    this.username = new String(usernameBytes, DEFAULT_CHAR_SET);
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.DISCONNECT_MESSAGE;
  }

  /**
   * get the username
   *
   * @return username username
   */
  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return "DisconnectMessage [username=" + username + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisconnectMessage that = (DisconnectMessage) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }
}
