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
 * The type Connect response test.
 */
class ConnectResponseTest {

  private boolean success;
  private String message;
  private ConnectResponse response;
  private ConnectResponse r2;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    success = true;
    message = "connected successfully";
    response = new ConnectResponse(success, message);
    r2 = new ConnectResponse(success, message);
  }

  /**
   * Encode and decode.
   *
   * @throws IOException the io exception
   */
  @Test
  void encodeAndDecode() throws IOException {
    // Act: Encode the message
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);
    response.encode(dataOut);
    dataOut.flush();

    // Decode the message
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    DataInputStream dataIn = new DataInputStream(byteArrayInputStream);
    Message decodedMessage = Message.decodeFromStream(dataIn);

    // Assert: Validate the integrity of the decoded message
    assertInstanceOf(ConnectResponse.class, decodedMessage);
    ConnectResponse resultMessage = (ConnectResponse) decodedMessage;
    assertEquals(response.getMessage(), resultMessage.getMessage());
  }

  /**
   * Gets message type.
   */
  @Test
  void getMessageType() {
    assertEquals(MessageType.CONNECT_RESPONSE, response.getMessageType());
  }

  /**
   * Is success.
   */
  @Test
  void isSuccess() {
    assertEquals(success, response.isSuccess());
  }

  /**
   * Gets message.
   */
  @Test
  void getMessage() {
    assertEquals(message, response.getMessage());
  }

  /**
   * To string test.
   */
  @Test
  void toStringTest() {
    assertEquals(r2.toString(), response.toString());
  }

  /**
   * Equals.
   */
  @Test
  void equals() {
    assertEquals(r2, response);
    assertEquals(response, response);
    assertNotEquals(null, response);
    assertNotEquals(r2, new Object());
  }

  /**
   * Hash code test.
   */
  @Test
  void hashCodeTest() {
    assertEquals(r2.hashCode(), response.hashCode());
  }
}