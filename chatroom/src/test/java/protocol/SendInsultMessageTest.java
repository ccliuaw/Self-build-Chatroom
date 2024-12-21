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
 * The type Send insult message test.
 */
class SendInsultMessageTest {

  private String senderUsername;
  private String recipientUsername;
  private SendInsultMessage sendInsultMessage;
  private SendInsultMessage sendInsultMessage2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    senderUsername = "Peter";
    recipientUsername = "Jack";
    sendInsultMessage = new SendInsultMessage(senderUsername, recipientUsername);
    sendInsultMessage2 = new SendInsultMessage(senderUsername, recipientUsername);
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
    sendInsultMessage.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(SendInsultMessage.class, decodedMessage);
    SendInsultMessage resultMessage = (SendInsultMessage) decodedMessage;
    assertEquals(sendInsultMessage.getMessageType(), resultMessage.getMessageType());
    assertEquals(sendInsultMessage.getSenderUsername(), resultMessage.getSenderUsername());
    assertEquals(sendInsultMessage.getRecipientUsername(), resultMessage.getRecipientUsername());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertEquals(sendInsultMessage.toString(), sendInsultMessage2.toString());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    assertEquals(sendInsultMessage, sendInsultMessage);
    assertEquals(sendInsultMessage, sendInsultMessage2);
    assertNotEquals(sendInsultMessage, null);
    assertNotEquals(sendInsultMessage, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    assertEquals(sendInsultMessage.hashCode(), sendInsultMessage2.hashCode());
  }
}