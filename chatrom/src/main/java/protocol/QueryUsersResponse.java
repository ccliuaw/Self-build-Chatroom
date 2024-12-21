package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represent a query users response message
 */
public class QueryUsersResponse extends Message {

  private List<String> connectedUsers = new ArrayList<>();

  /**
   * Default constructor
   */
  public QueryUsersResponse() {
  }

  /**
   * Construct a QueryUsersResponse with given input arguments:
   *
   * @param connectedUsers list of all the clients
   */
  public QueryUsersResponse(List<String> connectedUsers) {
    this.connectedUsers = connectedUsers;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue());
    out.writeInt(connectedUsers.size());
    for (String user : connectedUsers) {
      byte[] userBytes = (user != null ? user : EMPTY_STRING).getBytes(DEFAULT_CHAR_SET);
      out.writeInt(userBytes.length);
      out.write(userBytes);
    }
  }

  @Override
  public void decode(DataInputStream in) throws IOException {
    int userCount = in.readInt();
    connectedUsers = new ArrayList<>();
    for (int i = 0; i < userCount; i++) {
      int usernameSize = in.readInt();
      byte[] usernameBytes = new byte[usernameSize];
      in.readFully(usernameBytes);
      connectedUsers.add(new String(usernameBytes, DEFAULT_CHAR_SET));
    }
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.QUERY_USER_RESPONSE;
  }

  /**
   * get the list of all the clients
   *
   * @return the list of clients
   */
  public List<String> getConnectedUsers() {
    return connectedUsers;
  }

  @Override
  public String toString() {
    return "QueryUsersResponse [connectedUsers size=" + connectedUsers.size() + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryUsersResponse that = (QueryUsersResponse) o;
    return Objects.equals(connectedUsers, that.connectedUsers);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(connectedUsers);
  }
}
