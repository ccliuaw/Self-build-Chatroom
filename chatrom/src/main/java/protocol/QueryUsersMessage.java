package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a query users message
 */
public class QueryUsersMessage extends Message {

  private String username;

  /**
   * Default constructor
   */
  public QueryUsersMessage() {
  }

  /**
   * Construct a QueryUsersMessage with given input arguments:
   *
   * @param username the username of sender
   */
  public QueryUsersMessage(String username) {
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
    return MessageType.QUERY_CONNECTED_USERS;
  }

  /**
   * get the username
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return "QueryUsersMessage [username=" + username + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryUsersMessage that = (QueryUsersMessage) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(username);
  }
}
