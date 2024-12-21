package client;

import java.util.Objects;
import protocol.BroadcastMessage;
import protocol.ConnectResponse;
import protocol.DirectMessage;
import protocol.FailedMessage;
import protocol.Message;
import protocol.QueryUsersResponse;

/**
 * The type Server message handler.
 */
public class ServerMessageHandler {

  /**
   * Error message for unknown message types.
   */
  public static final String UNKNOWN_MESSAGE_TYPE_ERROR = "Unknown message type.";
  /**
   * Error message for failed login attempts.
   */
  public static final String CANT_LOGIN_IN_ERROR = "Can't log in. Unexpected response from server.";
  
  private final ChatUI chatUI;

  /**
   * Instantiates a new Server message handler.
   *
   * @param chatUI the chat ui
   */
  public ServerMessageHandler(ChatUI chatUI) {
    this.chatUI = chatUI;
  }

  /**
   * Handle server messages boolean.
   *
   * @param message the message
   * @return the boolean
   */
  public boolean handleServerMessages(Message message) {
    if (message instanceof BroadcastMessage broadcast) {
      chatUI.showBroadcastMessage(broadcast);
    } else if (message instanceof DirectMessage direct) {
      chatUI.showDirectMessage(direct);
    } else if (message instanceof FailedMessage failed) {
      chatUI.showFailedMessage(failed);
    } else if (message instanceof QueryUsersResponse queryResponse) {
      chatUI.showQueryUsersResponse(queryResponse);
    } else if (message instanceof ConnectResponse disconnectResponse) {
      chatUI.showDisconnectResponse(disconnectResponse);
      return false;
    } else {
      chatUI.showErrorMessage(UNKNOWN_MESSAGE_TYPE_ERROR);
    }
    return true;
  }

  /**
   * Handle login boolean.
   *
   * @param response the response
   * @return the boolean
   */
  public boolean handleLogin(Message response) {
    if (response instanceof ConnectResponse connectResponse) {
      if (connectResponse.isSuccess()) {
        chatUI.showConnectedMessage();
        chatUI.showServerMessage(connectResponse.getMessage());
        return true;
      } else {
        chatUI.showServerError(connectResponse.getMessage());
      }
    } else {
      chatUI.showServerError(CANT_LOGIN_IN_ERROR);
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerMessageHandler that = (ServerMessageHandler) o;
    return Objects.equals(chatUI, that.chatUI);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(chatUI);
  }

  @Override
  public String toString() {
    return "ServerMessageHandler{" +
        "chatUI=" + chatUI +
        '}';
  }
}
