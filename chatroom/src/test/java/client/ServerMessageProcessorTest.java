package client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Server message processor test.
 */
class ServerMessageProcessorTest {

  private ServerMessageProcessor serverMessageProcessor;
  private ByteArrayOutputStream baos;
  private DataOutputStream out;
  private ChatUI chatUI;

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    baos = new ByteArrayOutputStream();
    out = new DataOutputStream(baos);
    chatUI = new ChatUI(System.in);
  }

  /**
   * Test wait for chat connection.
   *
   * @throws IOException the io exception
   */
  @Test
  void testWaitForChatConnection() throws IOException {
    // Simulate ConnectResponse
    new ConnectResponse(true, "Connected to server").sendToStream(out);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInputStream in = new DataInputStream(bais);

    serverMessageProcessor = new ServerMessageProcessor(in, chatUI);
    assertTrue(serverMessageProcessor.waitForChatConnection());

    // Test failed connection
    baos.reset();
    new FailedMessage("Failed").sendToStream(out);

    bais = new ByteArrayInputStream(baos.toByteArray());
    in = new DataInputStream(bais);

    serverMessageProcessor = new ServerMessageProcessor(in, chatUI);
    assertFalse(serverMessageProcessor.waitForChatConnection());
  }

  /**
   * Test run.
   *
   * @throws IOException the io exception
   */
  @Test
  void testRun() throws IOException {
    // Simulate messages
    new BroadcastMessage("user", "Hello").sendToStream(out);

    new DirectMessage("user", "user", "Hello").sendToStream(out);

    new FailedMessage("Error").sendToStream(out);

    new QueryUsersResponse(List.of("user", "user2")).sendToStream(out);

    new ConnectResponse(true, "Disconnected!").sendToStream(out);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInputStream in = new DataInputStream(bais);

    serverMessageProcessor = new ServerMessageProcessor(in, chatUI);
    serverMessageProcessor.setConnectedToChat(true);

    Thread thread = new Thread(serverMessageProcessor);
    thread.start();

    try {
      thread.join(5000);
    } catch (InterruptedException e) {
      fail();
    }

    assertFalse(serverMessageProcessor.isConnectedToChat());
  }

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    ByteArrayInputStream bais1 = new ByteArrayInputStream("test".getBytes());
    ByteArrayInputStream bais2 = new ByteArrayInputStream("test2".getBytes());
    DataInputStream in1 = new DataInputStream(bais1);
    DataInputStream in2 = new DataInputStream(bais2);
    ChatUI chatUI1 = new ChatUI(System.in);
    ChatUI chatUI2 = new ChatUI(System.in);

    ServerMessageProcessor processor1 = new ServerMessageProcessor(in1, chatUI1);
    ServerMessageProcessor processor2 = new ServerMessageProcessor(in1, chatUI1);
    ServerMessageProcessor processor3 = new ServerMessageProcessor(in2, chatUI2);

    assertEquals(processor1, processor2);
    assertNotEquals(processor1, processor3);
    assertNotEquals(processor1, null);
    assertNotEquals(processor1, new Object());
  }

  /**
   * Test hash code.
   */
  @Test
  void testHashCode() {
    ByteArrayInputStream bais1 = new ByteArrayInputStream("test".getBytes());
    ByteArrayInputStream bais2 = new ByteArrayInputStream("test2".getBytes());
    DataInputStream in1 = new DataInputStream(bais1);
    DataInputStream in2 = new DataInputStream(bais2);
    ChatUI chatUI1 = new ChatUI(System.in);
    ChatUI chatUI2 = new ChatUI(System.in);

    ServerMessageProcessor processor1 = new ServerMessageProcessor(in1, chatUI1);
    ServerMessageProcessor processor2 = new ServerMessageProcessor(in1, chatUI1);
    ServerMessageProcessor processor3 = new ServerMessageProcessor(in2, chatUI2);

    assertEquals(processor1.hashCode(), processor2.hashCode());
    assertNotEquals(processor1.hashCode(), processor3.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ByteArrayInputStream bais = new ByteArrayInputStream("test".getBytes());
    DataInputStream in = new DataInputStream(bais);
    ChatUI chatUI = new ChatUI(System.in);

    ServerMessageProcessor processor = new ServerMessageProcessor(in, chatUI);
    String toString = processor.toString();

    assertTrue(toString.contains("ServerMessageProcessor"));
    assertTrue(toString.contains("in"));
    assertTrue(toString.contains("chatUI"));
    assertTrue(toString.contains("serverMessageHandler"));
    assertTrue(toString.contains("connectedToChat"));
  }
}