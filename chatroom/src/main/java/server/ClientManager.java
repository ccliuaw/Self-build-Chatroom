package server;

import java.util.List;
import java.util.Objects;
import protocol.Message;
import java.io.*;
import java.net.Socket;

/**
 * The type Client manager.
 */
public class ClientManager implements Runnable {

  /**
   * Prefix for connection-related error messages.
   */
  public static final String CONNECTION_ERROR_PREFIX = "Connection error: ";
  /**
   * Error message for failed client connection closure.
   */
  public static final String CLOSE_ERROR = "Error closing connection with client";
  
  private final Socket socket;
  private List<ClientManager> clients;
  private DataInputStream in;
  private DataOutputStream out;
  private String username = null;
  private boolean isInChatRoom;
  private ClientMessageHandler clientMessageHandler;

  /**
   * Instantiates a new Client manager.
   *
   * @param socket  the socket
   * @param clients the clients
   */
  public ClientManager(Socket socket, List<ClientManager> clients) {
    this.socket = socket;
    this.clients = clients;
  }

  /**
   * Initialize.
   *
   * @throws IOException the io exception
   */
  public void initialize() throws IOException {
    this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    this.clientMessageHandler = new ClientMessageHandler(out, clients);
  }

  public void run() {
    try {
      initialize();
      while (username == null) {
        Message message = Message.decodeFromStream(in);
        username = clientMessageHandler.handleLogin(message);
      }
      isInChatRoom = true;
      while (isInChatRoom) {
        Message message = Message.decodeFromStream(in);
        isInChatRoom = clientMessageHandler.handleMessage(message, username);
      }
    } catch (IOException e) {
      System.out.println(CONNECTION_ERROR_PREFIX + e.getMessage());
    } finally {
      cleanup();
    }
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Is in chat room boolean.
   *
   * @return the boolean
   */
  public boolean isInChatRoom() {
    return isInChatRoom;
  }

  /**
   * Gets out stream.
   *
   * @return the out stream
   */
  public DataOutputStream getOutStream() {
    return out;
  }

  private void cleanup() {
    clients.remove(this);
    try {
      socket.close();
    } catch (IOException e) {
      System.out.println(CLOSE_ERROR);
    }
  }

  /**
   * Gets client message handler.
   *
   * @return the client message handler
   */
  public ClientMessageHandler getClientMessageHandler() {
    return clientMessageHandler;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientManager that = (ClientManager) o;
    return Objects.equals(getUsername(),
        that.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername());
  }

  @Override
  public String toString() {
    return "ClientManager{" +
        "socket=" + socket +
        ", in=" + in +
        ", out=" + out +
        ", username='" + username + '\'' +
        ", isInChatRoom=" + isInChatRoom +
        ", clientMessageHandler=" + clientMessageHandler +
        '}';
  }
}
