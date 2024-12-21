package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.*;
import java.net.Socket;
import protocol.ConnectResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Chat client test.
 */
class ChatClientTest {

  private ChatClient chatClient;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private final InputStream originalIn = System.in;
  private TestSocket testSocket;

  /**
   * Sets up.
   *
   * @throws IOException the io exception
   */
  @BeforeEach
  void setUp() throws IOException {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    testSocket = new TestSocket();
  }

  /**
   * Test initialize with invalid input.
   *
   * @throws IOException the io exception
   */
  @Test
  void testInitializeWithInvalidInput() throws IOException {
    String input = "invalid\ninvalid\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    new ChatClient(inputStream).start();
    new ConnectResponse(true, "").encode(new DataOutputStream(testSocket.getOutputStream()));
    assertTrue(errContent.toString()
        .contains("Unable to connect to host. Check the host and port then try again."));
  }

  /**
   * Test start with valid input.
   *
   * @throws IOException the io exception
   */
  @Test
  void testStartWithValidInput() throws IOException {
    String input = "user\nlogoff\nexit\n";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    chatClient = new ChatClient(inputStream, testSocket);
    testSocket.getOutputStream().flush();
    //One connect response
    new ConnectResponse(true, "").sendToStream(new DataOutputStream(testSocket.getOutputStream()));
    //One disconnect response
    new ConnectResponse(true, "").sendToStream(new DataOutputStream(testSocket.getOutputStream()));
    chatClient.start();
    //toDisconnect
    assertTrue(outContent.toString().contains("Successfully Connected"));

  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    ChatClient client1 = new ChatClient(System.in);
    ChatClient client2 = new ChatClient(System.in);
    ChatClient client3 = new ChatClient(new ByteArrayInputStream("test".getBytes()));

    assertEquals(client1, client2);
    assertNotEquals(client1, client3);
    assertNotEquals(client1, null);
    assertNotEquals(client1, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    ChatClient client1 = new ChatClient(System.in);
    ChatClient client2 = new ChatClient(System.in);
    ChatClient client3 = new ChatClient(new ByteArrayInputStream("test".getBytes()));

    assertEquals(client1.hashCode(), client2.hashCode());
    assertNotEquals(client1.hashCode(), client3.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ChatClient client = new ChatClient(System.in);
    String toString = client.toString();

    assertTrue(toString.contains("ChatClient"));
    assertTrue(toString.contains("socket"));
    assertTrue(toString.contains("in"));
    assertTrue(toString.contains("out"));
    assertTrue(toString.contains("username"));
    assertTrue(toString.contains("chatMessageHandler"));
    assertTrue(toString.contains("serverMessageProcessor"));
  }

  /**
   * Restore streams.
   */
  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
    System.setIn(originalIn);
  }

  // Custom TestSocket class to simulate Socket behavior
  private static class TestSocket extends Socket {

    private PipedInputStream pis = new PipedInputStream();
    private PipedOutputStream pos = new PipedOutputStream(pis);

    private TestSocket() throws IOException {
    }

    @Override
    public OutputStream getOutputStream() {
      return pos;
    }

    @Override
    public InputStream getInputStream() {
      return pis;
    }

    @Override
    public boolean isConnected() {
      return true;
    }

    @Override
    public void close() {
      // Do nothing for test
    }
  }
}