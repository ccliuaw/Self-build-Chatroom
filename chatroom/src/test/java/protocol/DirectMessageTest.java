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
 * The type Direct message test.
 */
class DirectMessageTest {

  private String senderUsername;
  private String recipientUsername;
  private String message;
  private DirectMessage directMessage;
  private DirectMessage directMessage2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    senderUsername = "sender";
    recipientUsername = "recipient";
    message = "Hello, Jack";
    directMessage = new DirectMessage(senderUsername, recipientUsername, message);
    directMessage2 = new DirectMessage(senderUsername, recipientUsername, message);
  }

  /**
   * Encode ane decode.
   *
   * @throws IOException the io exception
   */
  @Test
  void encodeAneDecode() throws IOException {
    // Act: Encode the message
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);
    directMessage.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(DirectMessage.class, decodedMessage);
    DirectMessage resultMessage = (DirectMessage) decodedMessage;
    assertEquals(directMessage.getSenderUsername(), resultMessage.getSenderUsername());
    assertEquals(directMessage.getRecipientUsername(), resultMessage.getRecipientUsername());
    assertEquals(directMessage.getMessage(), resultMessage.getMessage());
  }

  /**
   * Gets message type.
   */
  @Test
  void getMessageType() {
    assertEquals(directMessage.getMessageType(), MessageType.DIRECT_MESSAGE);
  }

  /**
   * Gets sender username.
   */
  @Test
  void getSenderUsername() {
    assertEquals(directMessage.getSenderUsername(), senderUsername);
  }

  /**
   * Gets recipient username.
   */
  @Test
  void getRecipientUsername() {
    assertEquals(directMessage.getRecipientUsername(), recipientUsername);
  }

  /**
   * Gets message.
   */
  @Test
  void getMessage() {
    assertEquals(directMessage.getMessage(), message);
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals(directMessage.toString(), directMessage2.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(directMessage, directMessage);
    assertEquals(directMessage, directMessage2);
    assertNotEquals(directMessage, new Object());
    assertNotEquals(directMessage2, null);
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(directMessage.hashCode(), directMessage2.hashCode());
  }
}