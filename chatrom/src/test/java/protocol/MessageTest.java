package protocol;

import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Message test.
 */
public class MessageTest {

  /**
   * Test encode and decode connect message.
   *
   * @throws IOException the io exception
   */
  @Test
  public void testEncodeAndDecodeConnectMessage() throws IOException {
    // Create a ConnectMessage instance
    ConnectMessage originalMessage = new ConnectMessage("testUser");

    // Simulate a DataOutputStream
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);

    // Encode the message
    originalMessage.encode(dataOut);
    dataOut.flush();

    // Simulate a DataInputStream with the encoded data
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);

    // Decode the message
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Validate the message type and content
    assertTrue(decodedMessage instanceof ConnectMessage);
    ConnectMessage decodedConnectMessage = (ConnectMessage) decodedMessage;
    assertEquals("testUser", decodedConnectMessage.getUsername());
  }

  /**
   * Test encode and decode broadcast message.
   *
   * @throws IOException the io exception
   */
  @Test
  public void testEncodeAndDecodeBroadcastMessage() throws IOException {
    // Create a BroadcastMessage instance
    BroadcastMessage originalMessage = new BroadcastMessage("testUser", "Hello, world!");

    // Simulate a DataOutputStream
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);

    // Encode the message
    originalMessage.encode(dataOut);
    dataOut.flush();

    // Simulate a DataInputStream with the encoded data
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);

    // Decode the message
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Validate the message type and content
    assertTrue(decodedMessage instanceof BroadcastMessage);
    BroadcastMessage decodedBroadcastMessage = (BroadcastMessage) decodedMessage;
    assertEquals("testUser", decodedBroadcastMessage.getSenderUsername());
    assertEquals("Hello, world!", decodedBroadcastMessage.getMessage());
  }

  /**
   * Test invalid message type.
   */
  @Test
  public void testInvalidMessageType() {
    // Simulate an invalid message type
    byte[] invalidMessageTypeData = new byte[]{0, 0, 0, 99}; // Message type 99 does not exist

    // Use ByteArrayInputStream to simulate incoming data
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(invalidMessageTypeData);
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);

    // Assert that decoding an invalid message type throws an exception
    assertThrows(IOException.class, () -> {
      Message.decodeFromStream(dataIn);
    });
  }
}
