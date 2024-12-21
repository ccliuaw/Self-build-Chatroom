package server;

import client.ChatUI;
import client.ServerMessageProcessor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Client manager test.
 */
class ClientManagerTest {

  private ClientManager clientManager;
  private ByteArrayOutputStream baos;
  private DataOutputStream out;
  private TestSocket socket;
  /**
   * The Clients.
   */
  List<ClientManager> clients;


  /**
   * Sets up.
   *
   * @throws IOException the io exception
   */
  @BeforeEach
  void setUp() throws IOException {
    socket = new TestSocket();
    baos = new ByteArrayOutputStream();
    out = new DataOutputStream(socket.getOutputStream());
    clients = new ArrayList<>();
  }

  /**
   * Test run.
   *
   * @throws IOException the io exception
   */
  @Test
  void testRun() throws IOException {
    new ConnectMessage("user").sendToStream(out);
    // Simulate messages
    new BroadcastMessage("user", "Hello").sendToStream(out);

    new DirectMessage("user", "user", "Hello").sendToStream(out);

    new FailedMessage("Error").sendToStream(out);

    new QueryUsersResponse(List.of("user", "user2")).sendToStream(out);

    new DisconnectMessage("user").sendToStream(out);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInputStream in = new DataInputStream(bais);

    clientManager = new ClientManager(socket, clients);
    clients.add(clientManager);

    Thread thread = new Thread(clientManager);
    thread.start();

    try {
      thread.join(5000);
    } catch (InterruptedException e) {
      fail();
    }

    assertFalse(clientManager.isInChatRoom());
  }

  /**
   * Test equals.
   *
   * @throws IOException the io exception
   */
  @Test
  void testEquals() throws IOException {
    clientManager = new ClientManager(socket, clients);
    clients.add(clientManager);

    // Test reflexivity
    assertEquals(clientManager, clientManager);

    // Test symmetry
    ClientManager otherManager = new ClientManager(socket, clients);
    assertEquals(clientManager, otherManager);
    assertEquals(otherManager, clientManager);

    // Test null
    assertNotEquals(null, clientManager);

    // Test different class
    assertNotEquals(clientManager, new Object());

    // Test different username
    ClientManager differentUsernameManager = new ClientManager(socket, clients);
    differentUsernameManager.setUsername("differentUsername");
    assertNotEquals(clientManager, differentUsernameManager);

    // Test different clientMessageHandler
    ClientManager managerWithDifferentHandler = new ClientManager(socket, clients);
    managerWithDifferentHandler.setUsername("testUser");
    managerWithDifferentHandler.initialize();
    managerWithDifferentHandler.setUsername("testUser2");
    assertNotEquals(clientManager, managerWithDifferentHandler);
  }

  /**
   * Test hash code.
   *
   * @throws IOException the io exception
   */
  @Test
  void testHashCode() throws IOException {
    clientManager = new ClientManager(socket, clients);
    clientManager.setUsername("testUser");

    // Test consistency
    int initialHashCode = clientManager.hashCode();
    assertEquals(initialHashCode, clientManager.hashCode());

    assertEquals(clientManager.hashCode(), clientManager.hashCode());

    // Test different objects have different hashCodes
    ClientManager differentManager = new ClientManager(new Socket(), new ArrayList<>());
    differentManager.setUsername("testUser2");
    assertNotEquals(clientManager.hashCode(), differentManager.hashCode());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    clientManager = new ClientManager(socket, clients);
    String expectedString = "username='" + clientManager.getUsername();
    assertTrue(clientManager.toString().contains(expectedString));

    // Test with non-null username
    clientManager.setUsername("testUser");
    expectedString = "username='testUser'";
    assertTrue(clientManager.toString().contains(expectedString));
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
}