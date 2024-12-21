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
 * The type Disconnect message test.
 */
class DisconnectMessageTest {

  private String username;
  private DisconnectMessage disconnectMessage;
  private DisconnectMessage disconnectMessage2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    username = "testUser";
    disconnectMessage = new DisconnectMessage(username);
    disconnectMessage2 = new DisconnectMessage(username);
  }

  /**
   * Encode.
   *
   * @throws IOException the io exception
   */
  @Test
  void encode() throws IOException {
    // Act: Encode the message
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);
    disconnectMessage.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(DisconnectMessage.class, decodedMessage);
    DisconnectMessage resultMessage = (DisconnectMessage) decodedMessage;
    assertEquals(disconnectMessage.getUsername(), resultMessage.getUsername());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals(disconnectMessage2.toString(), disconnectMessage.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(disconnectMessage, disconnectMessage);
    assertEquals(disconnectMessage, disconnectMessage2);
    assertNotEquals(disconnectMessage, null);
    assertNotEquals(disconnectMessage, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(disconnectMessage.hashCode(), disconnectMessage2.hashCode());
  }
}