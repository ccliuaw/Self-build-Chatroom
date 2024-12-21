package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a connect message
 */
public class ConnectMessage extends Message {

  private String username;

  /**
   * Default constructor
   */
  public ConnectMessage() {
  }

  /**
   * Construct a ConnectMessage with given input arguments:
   *
   * @param username the username for requester
   */
  public ConnectMessage(String username) {
    this.username = username;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue()); // Message type
    byte[] usernameBytes = (username != null ? username : EMPTY_STRING).getBytes(
        DEFAULT_CHAR_SET);
    out.writeInt(usernameBytes.length); // Username size
    out.write(usernameBytes); // Username bytes
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
    return MessageType.CONNECT_MESSAGE;
  }

  /**
   * get the username
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectMessage that = (ConnectMessage) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }

  @Override
  public String toString() {
    return "Connect message for: " + username;
  }
}
