package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import protocol.ConnectResponse;

/**
 * Represent a Server class for the chat room
 */
public class Server {

  private static final int ANY_OPEN_PORT = 0;
  private static final int DEFAULT_PORT = 12345;
  private static final int MAX_CLIENTS = 10;
  /**
   * List of connected client managers.
   */
  public List<ClientManager> clients = new ArrayList<>();
  /**
   * Prefix for server port announcement message.
   */
  public static final String PORT_MESSAGE_PREFIX = "Server started on port ";
  /**
   * Prefix for new client connection announcement.
   */
  public static final String NEW_CLIENT_HOST_PREFIX = "New client connected from ";
  /**
   * Message sent when server reaches maximum client capacity.
   */
  public static final String SERVER_IS_FULL_MESSAGE = "Server is full. Try again later.";
  private final ServerSocket serverSocket;

  /**
   * Instantiates a new Server.
   *
   * @param serverSocket the server socket
   */
  public Server(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  /**
   * Instantiates a new Server.
   *
   * @throws IOException the io exception
   */
  public Server() throws IOException {
    ServerSocket serverSocket1;
    try {
      serverSocket1 = new ServerSocket(DEFAULT_PORT);
    } catch (IOException e) {
      serverSocket1 = new ServerSocket(ANY_OPEN_PORT);
    }
    this.serverSocket = serverSocket1;
  }

  /**
   * Main function for the server to start
   *
   * @throws IOException the io exception
   */
  public void start() throws IOException {
    System.out.println(PORT_MESSAGE_PREFIX + serverSocket.getLocalPort());
    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println(NEW_CLIENT_HOST_PREFIX + socket.getInetAddress());
      if (clients.size() < MAX_CLIENTS) {
        ClientManager clientManager = new ClientManager(socket, clients);
        clients.add(clientManager);
        new Thread(clientManager).start();
      } else {
        new ConnectResponse(false, SERVER_IS_FULL_MESSAGE).sendToStream(
            new DataOutputStream((socket.getOutputStream())));
        socket.close();
      }
    }
  }
}

