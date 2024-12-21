package protocol;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Query users response test.
 */
class QueryUsersResponseTest {

  private List<String> connectedUsers;
  private QueryUsersResponse queryUsersResponse;
  private QueryUsersResponse queryUsersResponse2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    connectedUsers = new ArrayList<>();
    connectedUsers.add("user1");
    connectedUsers.add("user2");
    connectedUsers.add("user3");
    queryUsersResponse = new QueryUsersResponse(connectedUsers);
    queryUsersResponse2 = new QueryUsersResponse(connectedUsers);
  }

  /**
   * Encode and decode.
   *
   * @throws IOException the io exception
   */
  @Test
  void encodeAndDecode() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);
    queryUsersResponse.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(QueryUsersResponse.class, decodedMessage);
    QueryUsersResponse resultMessage = (QueryUsersResponse) decodedMessage;
    assertEquals(queryUsersResponse.getConnectedUsers(), resultMessage.getConnectedUsers());
    assertEquals(queryUsersResponse.getMessageType(), resultMessage.getMessageType());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals(queryUsersResponse.toString(), queryUsersResponse2.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(queryUsersResponse, queryUsersResponse);
    assertEquals(queryUsersResponse, queryUsersResponse2);
    assertNotEquals(queryUsersResponse2, null);
    assertNotEquals(queryUsersResponse, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(queryUsersResponse.hashCode(), queryUsersResponse2.hashCode());
  }
}