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
 * The type Connect message test.
 */
class ConnectMessageTest {

  private ConnectMessage originalMessage;
  private String username;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    username = "testUser";
    originalMessage = new ConnectMessage(username);
  }

  /**
   * Encode and decode test.
   *
   * @throws IOException the io exception
   */
  @Test
  void encodeAndDecodeTest() throws IOException {
    // Act: Encode the message
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);
    originalMessage.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(ConnectMessage.class, decodedMessage);
    ConnectMessage resultMessage = (ConnectMessage) decodedMessage;
    assertEquals(originalMessage.getUsername(), resultMessage.getUsername());

  }

  /**
   * Gets message type.
   */
  @Test
  void getMessageType() {
    assertEquals(originalMessage.getMessageType(), MessageType.CONNECT_MESSAGE);
  }

  /**
   * Gets username.
   */
  @Test
  void getUsername() {
    assertEquals(originalMessage.getUsername(), username);
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(originalMessage, originalMessage);
    assertEquals(originalMessage, new ConnectMessage(username));
    assertNotEquals(originalMessage, null);
    assertNotEquals(originalMessage, new Object());
    assertNotEquals(originalMessage, new ConnectMessage("peter"));
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    ConnectMessage connectMessage = new ConnectMessage(username);
    assertEquals(originalMessage.hashCode(), connectMessage.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ConnectMessage connectMessage = new ConnectMessage(username);
    assertEquals(originalMessage.toString(), connectMessage.toString());
  }
}