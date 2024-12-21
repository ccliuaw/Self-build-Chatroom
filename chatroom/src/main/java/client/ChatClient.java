package client;

import java.io.*;
import java.net.*;
import java.util.Objects;

/**
 * Represent a client who want to connect to the chat
 */
public class ChatClient {

  /**
   * Error message for failed host connection attempts.
   */
  public static final String INITIALIZE_HOST_ERROR_MESSAGE = "Unable to connect to host. Check the host and port then try again.";

  /**
   * Prefix for connection-related error messages.
   */
  public static final String CONNECTION_ERROR_PREFIX = "Connection error. ";

  /**
   * Prefix for cleanup process error messages.
   */
  public static final String ERROR_CLEANING_UP_PREFIX = "Error cleaning up. ";
  private Socket socket;
  private DataInputStream in;
  private DataOutputStream out;
  private String username;
  /**
   * The Chat ui.
   */
  ChatUI chatUI;
  private ChatMessageHandler chatMessageHandler;
  private ServerMessageProcessor serverMessageProcessor;

  /**
   * Construct a ChatClient instance from input stream
   *
   * @param in the in
   */
  public ChatClient(InputStream in) {
    chatUI = new ChatUI(in);
  }

  /**
   * Construct a ChatClient instance from input stream
   *
   * @param in     the in
   * @param socket the socket
   */
  public ChatClient(InputStream in, Socket socket) {
    chatUI = new ChatUI(in);
    this.socket = socket;
  }

  private void initialize() throws IOException {

    try {
      if (socket == null || !socket.isConnected()) {
        socket = this.initializeClient(chatUI.getHostFromUser(), chatUI.getPortFromUser());
      }
      // Ask client for server host and port and connect to server
      in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
      serverMessageProcessor = new ServerMessageProcessor(in, chatUI);
      chatMessageHandler = new ChatMessageHandler(out, chatUI);
    } catch (IOException | NumberFormatException e) {
      throw new IOException(INITIALIZE_HOST_ERROR_MESSAGE);
    }
  }

  /**
   * start the connection to the server and handle the message sending
   */
  public void start() {
    try {
      initialize();
      while (!serverMessageProcessor.isConnectedToChat()) {
        // Login and establish connection
        this.username = chatUI.login();
        chatMessageHandler.connectToChat(username);
        boolean result = serverMessageProcessor.waitForChatConnection();
        serverMessageProcessor.setConnectedToChat(result);
      }

      chatUI.displayWelcomeToChatMessage();
      // Start listening for server messages
      new Thread(serverMessageProcessor).start();

      while (true) {
        // Start handling user input
        String input = chatUI.getChatBoxInput(username);
        if (!serverMessageProcessor.isConnectedToChat()) {
          break;
        }
        chatMessageHandler.handleChatInput(input);
      }
    } catch (IOException e) {
      chatUI.showErrorMessage(
          CONNECTION_ERROR_PREFIX + (e.getMessage() != null ? e.getMessage() : ""));
    } finally {
      cleanup();
    }
  }

  /**
   * initialize the client with info:
   *
   * @param host provided host IP address or host name
   * @param port provided port for the server
   * @return socket from given info
   * @throws IOException for exception
   */
  private Socket initializeClient(String host, String port)
      throws IOException, NumberFormatException {
    return new Socket(host, Integer.parseInt(port));
  }

  private void cleanup() {
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
      chatUI.cleanup();
    } catch (IOException e) {
      System.err.println(ERROR_CLEANING_UP_PREFIX + e.getMessage());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatClient that = (ChatClient) o;
    return Objects.equals(socket, that.socket) && Objects.equals(in, that.in)
        && Objects.equals(out, that.out) && Objects.equals(username,
        that.username) && Objects.equals(chatUI, that.chatUI) && Objects.equals(
        chatMessageHandler, that.chatMessageHandler) && Objects.equals(
        serverMessageProcessor, that.serverMessageProcessor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(socket, in, out, username, chatUI, chatMessageHandler,
        serverMessageProcessor);
  }

  @Override
  public String toString() {
    return "ChatClient{" +
        "socket=" + socket +
        ", in=" + in +
        ", out=" + out +
        ", username='" + username + '\'' +
        ", chatMessageHandler=" + chatMessageHandler +
        ", serverMessageProcessor=" + serverMessageProcessor +
        '}';
  }
}

