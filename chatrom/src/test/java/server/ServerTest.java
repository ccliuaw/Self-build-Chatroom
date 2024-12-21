package server;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import protocol.ConnectResponse;
import protocol.Message;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Server test.
 */
class ServerTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private ExecutorService executor;

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

    @Override
    public boolean equals(Object obj) {
      return false;
      //for testing
    }
  }

  private static class TestServerSocket extends ServerSocket {

    private PipedInputStream pis = new PipedInputStream();
    private PipedOutputStream pos = new PipedOutputStream(pis);
    /**
     * The Max accept.
     */
    int maxAccept = 10;
    private List<TestSocket> testSockets = new ArrayList<>();
    private TestSocket lastTestSocket;

    /**
     * Instantiates a new Test server socket.
     *
     * @param maxAccept the max accept
     * @throws IOException the io exception
     */
    public TestServerSocket(int maxAccept) throws IOException {
      this.maxAccept = maxAccept;
    }


    @Override
    public Socket accept() throws IOException {
      while (maxAccept > testSockets.size()) {
        lastTestSocket = new TestSocket();
        testSockets.add(lastTestSocket);
        return lastTestSocket;
      }
      return lastTestSocket;
    }

    @Override
    public void close() {
      // Do nothing for test
    }

    /**
     * Gets last test socket.
     *
     * @return the last test socket
     */
    public TestSocket getLastTestSocket() {
      return lastTestSocket;
    }
  }

  /**
   * Sets up.
   */
  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    executor = Executors.newSingleThreadExecutor();
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
    executor.shutdownNow();
  }

  /**
   * Test server start and client connections.
   *
   * @throws IOException          the io exception
   * @throws InterruptedException the interrupted exception
   */
  @Test
  void testServerStartAndClientConnections() throws IOException, InterruptedException {
    TestServerSocket testServerSocket = new TestServerSocket(11);
    Thread thread = new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          new Server(testServerSocket).start();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
    thread.start();

    // Wait for server to accept enough connections
    thread.join(1000);

    // The 11th connection should be rejected
    assertTrue(Message.decodeFromStream(new DataInputStream(
        testServerSocket.getLastTestSocket().getInputStream())) instanceof ConnectResponse);

    String output = outContent.toString();
    assertTrue(output.contains("Server started on port"));
    assertTrue(output.contains("New client connected from"));
  }
}