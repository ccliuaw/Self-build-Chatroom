package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Client message handler test.
 */
class ClientMessageHandlerTest {

  private ClientMessageHandler handler;
  private ByteArrayOutputStream outContent;
  private List<ClientManager> clients;
  private TestSocket socket;

  /**
   * Sets up.
   *
   * @throws IOException the io exception
   */
  @BeforeEach
  void setUp() throws IOException {
    outContent = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(outContent);
    clients = new ArrayList<>();

    // Add a test client
    socket = new TestSocket();
//    new ConnectMessage("User1").sendToStream(new DataOutputStream(socket.getOutputStream()));
    ClientManager testClient = new ClientManager(socket, clients);
    testClient.initialize();
    handler = testClient.getClientMessageHandler();
    testClient.setUsername("testUser");
    clients.add(testClient);
  }

  /**
   * Test handle message.
   *
   * @throws IOException the io exception
   */
  @Test
  void testHandleMessage() throws IOException {
    // Test BroadcastMessage

    assertTrue(
        handler.handleMessage(new BroadcastMessage("sender", "Hello, everyone!"), "testUser"));
    assertTrue(
        handler.handleMessage(new BroadcastMessage("sender", "Hello, everyone!"), "testUser"));
//    assertTrue(Message.decodeFromStream(new DataInputStream(socket.getInputStream())) instanceof BroadcastMessage);

    // Test DirectMessage
    assertTrue(handler.handleMessage(new DirectMessage("sender", "testUser", "Hello, testUser!"),
        "sender"));
    assertTrue(handler.handleMessage(new DirectMessage("sender", "testUser", "Hello, testUser!"),
        "other"));
//    assertTrue(outContent.toString().contains("Hello, testUser!"));

    // Test QueryUsersMessage
    assertTrue(handler.handleMessage(new QueryUsersMessage("sender"), "sender"));
    assertTrue(handler.handleMessage(new QueryUsersMessage("sender"), "other"));
//    assertTrue(outContent.toString().contains("testUser"));

    // Test DisconnectMessage
    assertFalse(handler.handleMessage(new DisconnectMessage("sender"), "sender"));
    assertFalse(handler.handleMessage(new DisconnectMessage("sender"), "other"));
//    assertTrue(outContent.toString().contains("You are no longer connected"));

    // Test SendInsultMessage
    assertTrue(handler.handleMessage(new SendInsultMessage("sender", "testUser"), "sender"));
    assertTrue(handler.handleMessage(new SendInsultMessage("sender", "testUser"), "other"));
//    assertTrue(outContent.toString().contains("SERVER"));

    // Test invalid message
    assertTrue(handler.handleMessage(new Message() {
      @Override
      public void encode(DataOutputStream out) {
      }

      @Override
      public void decode(DataInputStream in) {
      }

      @Override
      public MessageType getMessageType() {
        return null;
      }
    }, "sender"));
//    assertTrue(outContent.toString().contains("Invalid message"));
  }

  /**
   * Test handle login.
   *
   * @throws IOException the io exception
   */
  @Test
  void testHandleLogin() throws IOException {
    // Test valid login
    assertEquals("validUser", handler.handleLogin(new ConnectMessage("validUser")));
//    assertTrue(outContent.toString().contains("There are 1 other connected clients"));

    // Test invalid login (empty username)
    assertNull(handler.handleLogin(new ConnectMessage("")));
//    assertTrue(outContent.toString().contains("Invalid username"));

    // Test invalid login message
    assertNull(handler.handleLogin(new DisconnectMessage("invalidUser")));
//    assertTrue(outContent.toString().contains("Invalid connect message"));
  }

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

  /**
   * Test equals.
   */
  @Test
  void testEquals() {
    // Test reflexivity
    assertEquals(handler, handler);

    // Test symmetry
    assertEquals(handler, handler);

    // Test null
    assertNotEquals(null, handler);

    // Test different class
    assertNotEquals(handler, new Object());

    // Test different DataOutputStream
    ClientMessageHandler differentOutHandler = new ClientMessageHandler(
        new DataOutputStream(new ByteArrayOutputStream()), clients);
    assertNotEquals(handler, differentOutHandler);

    // Test different clients List
    List<ClientManager> differentClients = new ArrayList<>();
    ClientMessageHandler differentClientsHandler = new ClientMessageHandler(
        new DataOutputStream(outContent), differentClients);
    assertNotEquals(handler, differentClientsHandler);
  }

  /**
   * Test to string.
   *
   * @throws IOException the io exception
   */
  @Test
  void testToString() throws IOException {
    String expectedString = "ClientMessageHandler{out=";
    assertTrue(handler.toString().contains(expectedString));
  }
}