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
 * The type Failed message test.
 */
class FailedMessageTest {

  private String message;
  private FailedMessage failedMessage;
  private FailedMessage failedMessage2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    message = "transport message fail";
    failedMessage = new FailedMessage(message);
    failedMessage2 = new FailedMessage(message);
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
    failedMessage.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(FailedMessage.class, decodedMessage);
    FailedMessage resultMessage = (FailedMessage) decodedMessage;
    assertEquals(failedMessage.getMessage(), resultMessage.getMessage());
    assertEquals(failedMessage.getMessageType(), resultMessage.getMessageType());
    assertEquals(failedMessage.getMessageType(), MessageType.FAILED_MESSAGE);
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals(failedMessage2.toString(), failedMessage.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(failedMessage, failedMessage);
    assertEquals(failedMessage, failedMessage2);
    assertNotEquals(failedMessage, null);
    assertNotEquals(failedMessage, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(failedMessage.hashCode(), failedMessage2.hashCode());
  }
}