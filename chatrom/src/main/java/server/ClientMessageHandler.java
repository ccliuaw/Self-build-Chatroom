package server;

import java.util.List;
import java.util.Objects;
import protocol.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * The type Client message handler.
 */
public class ClientMessageHandler {

  /**
   * Error message for invalid or malformed messages.
   */
  public static final String INVALID_MESSAGE = "Invalid message.";

  /**
   * Error message for an invalid recipient username.
   */
  public static final String INVALID_RECIPIENT = "Invalid recipient";

  /**
   * Arrow symbol used to format insults in the chatroom.
   */
  public static final String INSULT_ARROW = " -> ";

  /**
   * Separator used between usernames and messages.
   */
  public static final String COLON_SEPARATOR = ": ";

  /**
   * Default name for the server in messages.
   */
  public static final String SERVER_DEFAULT_NAME = "SERVER";

  /**
   * Error message for invalid connect responses from the server.
   */
  public static final String INVALID_CONNECT_RESPONSE =
      "Invalid username. Please send a connect message with a valid username";

  /**
   * Prefix used to indicate a username attempting to connect.
   */
  public static final String CONNECTING_USERNAME_PREFIX = "Connecting username: ";

  /**
   * Suffix indicating that a user has joined the chatroom.
   */
  public static final String JOINED_CHAT_PREFIX = " has joined the chat";

  /**
   * Error message for an invalid connect message received by the server.
   */
  public static final String INVALID_CONNECT_MESSAGE_ERROR = "Invalid connect message";
  private final DataOutputStream out;
  private List<ClientManager> clients;

  /**
   * Instantiates a new Client message handler.
   *
   * @param out     the out
   * @param clients the clients
   */
  public ClientMessageHandler(DataOutputStream out, List<ClientManager> clients) {
    this.out = out;
    this.clients = clients;
  }

  /**
   * Handle message boolean.
   *
   * @param message         the message
   * @param currentUsername the current username
   * @return the boolean
   * @throws IOException the io exception
   */
  public boolean handleMessage(Message message, String currentUsername) throws IOException {
    if (message instanceof BroadcastMessage broadcast) {
      broadcastMessage(broadcast.getMessage(), broadcast.getSenderUsername());
    } else if (message instanceof DirectMessage direct) {
      handleDirectMessage(direct);
    } else if (message instanceof QueryUsersMessage query) {
      handleQueryUsers(query.getUsername());
    } else if (message instanceof DisconnectMessage disconnect) {
      handleDisconnect(disconnect.getUsername(), currentUsername);
      return false;
    } else if (message instanceof SendInsultMessage insult) {
      handleSendInsult(insult.getSenderUsername(), insult.getRecipientUsername());
    } else {
      sendFailedMessage(INVALID_MESSAGE);
    }
    return true;
  }

  private void broadcastMessage(String message, String senderUsername) throws IOException {
    for (ClientManager client : clients) {
      if (client.isInChatRoom()) {
        new BroadcastMessage(senderUsername, message).sendToStream(client.getOutStream());
      }
    }
  }

  private void handleDirectMessage(DirectMessage direct) throws IOException {
    ClientManager recipient = getClientHandler(direct.getRecipientUsername());
    if (recipient == null) {
      sendFailedMessage(INVALID_RECIPIENT);
      return;
    }
    direct.sendToStream(recipient.getOutStream());
  }

  private void handleQueryUsers(String requesterUsername) throws IOException {
    List<String> connectedUsers = clients.stream()
        .map(ClientManager::getUsername)
        .filter(uname -> !uname.equals(requesterUsername)).toList();
    new QueryUsersResponse(connectedUsers).sendToStream(out);
  }

  private void handleDisconnect(String disconnectingUsername, String currentUsername)
      throws IOException {
    //Users can only disconnect themselves
    if (disconnectingUsername.equals(currentUsername)) {
      new ConnectResponse(true, "You are no longer connected.").sendToStream(out);
    } else {
      new ConnectResponse(false, "Can't disconnect other users.").sendToStream(out);
    }
  }

  private void handleSendInsult(String sender, String recipient) throws IOException {
    ClientManager recipientHandler = getClientHandler(recipient);
    if (recipientHandler == null) {
      sendFailedMessage(INVALID_RECIPIENT);
      return;
    }
    String insult = InsultGenerator.generateInsult();
    broadcastMessage(sender + INSULT_ARROW + recipient + COLON_SEPARATOR + insult,
        SERVER_DEFAULT_NAME);
  }

  private void sendFailedMessage(String errorMessage) throws IOException {
    new FailedMessage(errorMessage).sendToStream(out);
  }

  private ClientManager getClientHandler(String username) {
    return clients.stream()
        .filter(client -> client.getUsername().equals(username))
        .findFirst()
        .orElse(null);
  }

  /**
   * Handle login string.
   *
   * @param message the message
   * @return the string
   * @throws IOException the io exception
   */
  public String handleLogin(Message message) throws IOException {
    if (message instanceof ConnectMessage connectMessage) {
      String clientUsername = connectMessage.getUsername();
      if (clientUsername == null || clientUsername.trim().isEmpty()) {
        //Send failed connect response
        new ConnectResponse(false,
            INVALID_CONNECT_RESPONSE).sendToStream(
            out);
      } else {//Send successful Connect Response
        System.out.println(CONNECTING_USERNAME_PREFIX + clientUsername);
        new ConnectResponse(true,
            "There are " + (clients.size() - 1) + " other connected clients.").sendToStream(out);
        broadcastMessage(clientUsername + JOINED_CHAT_PREFIX, SERVER_DEFAULT_NAME);
        System.out.println(clientUsername + JOINED_CHAT_PREFIX);
        return clientUsername;
      }
    } else {
      sendFailedMessage(INVALID_CONNECT_MESSAGE_ERROR);
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientMessageHandler that = (ClientMessageHandler) o;
    return Objects.equals(out, that.out);
  }

  @Override
  public int hashCode() {
    return Objects.hash(out);
  }

  @Override
  public String toString() {
    return "ClientMessageHandler{" +
        "out=" + out +
        '}';
  }
}
