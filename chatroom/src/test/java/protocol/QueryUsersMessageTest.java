package protocol;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Query users message test.
 */
class QueryUsersMessageTest {

  private QueryUsersMessage queryUsersMessage;
  private QueryUsersMessage queryUsersMessage2;
  private String username;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    username = "testUser";
    queryUsersMessage = new QueryUsersMessage(username);
    queryUsersMessage2 = new QueryUsersMessage(username);
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
    queryUsersMessage.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(QueryUsersMessage.class, decodedMessage);
    QueryUsersMessage resultMessage = (QueryUsersMessage) decodedMessage;
    assertEquals(queryUsersMessage.getUsername(), resultMessage.getUsername());
    assertEquals(queryUsersMessage.getMessageType(), resultMessage.getMessageType());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals(queryUsersMessage.toString(), queryUsersMessage2.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(queryUsersMessage, queryUsersMessage);
    assertEquals(queryUsersMessage, queryUsersMessage2);
    assertNotEquals(queryUsersMessage, null);
    assertNotEquals(queryUsersMessage, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(queryUsersMessage.hashCode(), queryUsersMessage2.hashCode());
  }
}