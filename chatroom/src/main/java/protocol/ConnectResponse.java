package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Represent a connect response message
 */
public class ConnectResponse extends Message {

  private boolean success;
  private String message;

  /**
   * Default constructor
   */
  public ConnectResponse() {
  }

  /**
   * Construct a ConnectResponse with given input arguments:
   *
   * @param success connection successful or not
   * @param message the message content
   */
  public ConnectResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  @Override
  public void encode(DataOutputStream out) throws IOException {
    out.writeInt(getMessageType().getValue()); // Message type
    out.writeBoolean(success); // Success flag
    byte[] messageBytes = message.getBytes(DEFAULT_CHAR_SET);
    out.writeInt(messageBytes.length); // Message size
    out.write(messageBytes); // Message bytes
  }

  @Override
  public void decode(DataInputStream in) throws IOException {
    success = in.readBoolean();
    int messageSize = in.readInt();
    byte[] messageBytes = new byte[messageSize];
    in.readFully(messageBytes);
    this.message = new String(messageBytes, DEFAULT_CHAR_SET);
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.CONNECT_RESPONSE;
  }

  /**
   * get the connection result
   *
   * @return is success or not
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * get the message content
   *
   * @return message content
   */
  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConnectResponse that = (ConnectResponse) o;
    return success == that.success && Objects.equals(message, that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, message);
  }

  @Override
  public String toString() {
    return "ConnectResponse: " + success + ", " + message;
  }
}
