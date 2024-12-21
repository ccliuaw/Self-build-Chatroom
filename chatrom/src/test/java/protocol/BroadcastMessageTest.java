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
 * The type Broadcast message test.
 */
class BroadcastMessageTest {

  private BroadcastMessage originalMessage;
  private String sender;
  private String message;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    sender = "testUser";
    message = "Hello, World!";
    originalMessage = new BroadcastMessage(sender, message);
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
    assertInstanceOf(BroadcastMessage.class, decodedMessage);
    BroadcastMessage resultMessage = (BroadcastMessage) decodedMessage;
    assertEquals(originalMessage.getSenderUsername(), resultMessage.getSenderUsername());
    assertEquals(originalMessage.getMessage(), resultMessage.getMessage());

  }

  /**
   * Gets message type.
   */
  @Test
  void getMessageType() {
    assertEquals(originalMessage.getMessageType(), MessageType.BROADCAST_MESSAGE);
  }

  /**
   * Gets sender username.
   */
  @Test
  void getSenderUsername() {
    assertEquals(originalMessage.getSenderUsername(), sender);
  }

  /**
   * Gets message.
   */
  @Test
  void getMessage() {
    assertEquals(originalMessage.getMessage(), message);
  }

  /**
   * Equal test.
   */
  @Test
  void equalTest() {
    assertEquals(originalMessage, originalMessage);
    assertEquals(originalMessage, new BroadcastMessage(sender, message));
    assertNotEquals(originalMessage, null);
    assertNotEquals(originalMessage, new Object());
  }

  /**
   * Hash code test.
   */
  @Test
  void hashCodeTest() {
    assertEquals(originalMessage.hashCode(), new BroadcastMessage(sender, message).hashCode());
  }

  /**
   * To string test.
   */
  @Test
  void toStringTest() {
    assertEquals(originalMessage.toString(), new BroadcastMessage(sender, message).toString());
  }
}