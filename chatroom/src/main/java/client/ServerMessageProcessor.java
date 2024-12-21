package client;

import static client.ChatUI.CHAT_DISCONNECTED;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;
import protocol.Message;

/**
 * Represent a Handler for server to client
 */
public class ServerMessageProcessor implements Runnable {

  /**
   * Invalid Response From Server Error Message.
   */
  public static final String INVALID_RESPONSE_ERROR = "Invalid response from server";
  private final DataInputStream in;
  private final ChatUI chatUI;
  private final ServerMessageHandler serverMessageHandler;
  private boolean connectedToChat;

  /**
   * Construct a ServerToClientHandler with given input:
   *
   * @param in     DataInputStream
   * @param chatUI ChatUI
   */
  public ServerMessageProcessor(DataInputStream in, ChatUI chatUI) {
    this.in = in;
    this.chatUI = chatUI;
    serverMessageHandler = new ServerMessageHandler(chatUI);
  }

  @Override
  public void run() {
    while (connectedToChat) {
      try {
        Message message = Message.decodeFromStream(in);
        connectedToChat = serverMessageHandler.handleServerMessages(message);
      } catch (IOException e) {
        chatUI.showErrorMessage(INVALID_RESPONSE_ERROR);
        chatUI.showErrorMessage(CHAT_DISCONNECTED);
        connectedToChat = false;
      }
    }
  }

  /**
   * trying to connect to the server
   *
   * @return false if connection failed
   * @throws IOException for exception
   */
  public boolean waitForChatConnection() throws IOException {
    Message response = Message.decodeFromStream(in);
    return serverMessageHandler.handleLogin(response);
  }


  /**
   * Is connected to chat boolean.
   *
   * @return the boolean
   */
  public boolean isConnectedToChat() {
    return connectedToChat;
  }

  /**
   * Sets connected to chat.
   *
   * @param connectedToChat the connected to chat
   */
  public void setConnectedToChat(boolean connectedToChat) {
    this.connectedToChat = connectedToChat;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerMessageProcessor that = (ServerMessageProcessor) o;
    return isConnectedToChat() == that.isConnectedToChat() && Objects.equals(in, that.in)
        && Objects.equals(chatUI, that.chatUI) && Objects.equals(
        serverMessageHandler, that.serverMessageHandler);
  }

  @Override
  public int hashCode() {
    return Objects.hash(in, chatUI, serverMessageHandler, isConnectedToChat());
  }

  @Override
  public String toString() {
    return "ServerMessageProcessor{" +
        "in=" + in +
        ", chatUI=" + chatUI +
        ", serverMessageHandler=" + serverMessageHandler +
        ", connectedToChat=" + connectedToChat +
        '}';
  }
}
