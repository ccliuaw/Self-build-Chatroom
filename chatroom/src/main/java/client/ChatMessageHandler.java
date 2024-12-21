package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import protocol.BroadcastMessage;
import protocol.ConnectMessage;
import protocol.DirectMessage;
import protocol.DisconnectMessage;
import protocol.QueryUsersMessage;
import protocol.SendInsultMessage;

/**
 * Represent the Handler for client to server
 */
public class ChatMessageHandler {

  private static final String INSULT_MARKER = "!";
  private static final String COMMAND_PREFIX_AT = "@";

  /**
   * Help command symbol.
   */
  public static final String HELP_COMMAND = "?";
  /**
   * Escaped help command for regex.
   */
  private static final String HELP_COMMAND_ESCAPED = "\\" + HELP_COMMAND;
  /**
   * Command to log off from chat.
   */
  private static final String LOGOFF_COMMAND = "logoff";
  /**
   * Command to query connected users.
   */
  private static final String QUERY_USERS_COMMAND = "who";
  /**
   * Keyword for broadcast messages.
   */
  public static final String BROADCAST_KEYWORD = "all";
  /**
   * Full command for broadcasting messages.
   */
  private static final String BROADCAST_COMMAND = COMMAND_PREFIX_AT + BROADCAST_KEYWORD;
  /**
   * Help message displaying available commands.
   */
  public static final String HELP_MESSAGE = "Help instructions: \n"
      + LOGOFF_COMMAND + ": exit the chat room\n"
      + QUERY_USERS_COMMAND + ": find out who else in the chat\n"
      + COMMAND_PREFIX_AT + "<username>: sends a private message to the specified user\n"
      + BROADCAST_COMMAND + ": sends a broadcast message to " + BROADCAST_KEYWORD + " users\n"
      + INSULT_MARKER + "<username>: sends a random insult message to the specified user\n"
      + HELP_COMMAND + ": shows this help message\n";
  /**
   * Error message for empty chat messages.
   */
  public static final String EMPTY_MESSAGE_ERROR = "Error. Message cannot be empty.\n";
  /**
   * Error message for unrecognized chat commands.
   */
  public static final String UNKNOWN_COMMAND_ERROR = "Unknown command.\n";
  /**
   * Error message for failed insult message sending.
   */
  public static final String INSULT_SEND_FAILED_ERROR = "Failed to send Send Insult Message";
  /**
   * Error message for failed query users message sending.
   */
  public static final String QUERY_USERS_FAILED_ERROR = "Failed to send Query Users Message";
  /**
   * Error message for failed broadcast message sending.
   */
  public static final String BROADCAST_SEND_ERROR = "Failed to send broadcast";
  /**
   * Error message for failed direct message sending.
   */
  public static final String DIRECT_SEND_ERROR = "Failed to send direct message";
  /**
   * Error message for failed disconnect message sending.
   */
  public static final String DISCONNECT_FAILED_ERROR = "Failed to send disconnect";

  private final DataOutputStream out;
  private final ChatUI chatUI;
  private String username;

  /**
   * Construct a ClientToServerHandler instance with given input:
   *
   * @param out    DataOutputStream
   * @param chatUI ChatUI
   */
  public ChatMessageHandler(DataOutputStream out, ChatUI chatUI) {
    this.out = out;
    this.chatUI = chatUI;
  }

  /**
   * handle the user input in the console
   *
   * @param input input string
   */
  public void handleChatInput(String input) {
    // Patterns for different commands
    Pattern helpPattern = Pattern.compile("^" + HELP_COMMAND_ESCAPED + "$");
    Pattern logoffPattern = Pattern.compile("^" + LOGOFF_COMMAND + "$");
    Pattern whoPattern = Pattern.compile("^" + QUERY_USERS_COMMAND + "$");
    Pattern insultPattern = Pattern.compile("^" + INSULT_MARKER + "(\\S+)$");
    Pattern broadcastPattern = Pattern.compile("^" + COMMAND_PREFIX_AT + BROADCAST_KEYWORD
        + "\\s?(.*)$");
    Pattern directMessagePattern = Pattern.compile(COMMAND_PREFIX_AT + "(\\S+)\\s?(.*)");

    Matcher matcher;
    input = input.toLowerCase().trim();

    if ((helpPattern.matcher(input)).matches()) {
      chatUI.showMessage(HELP_MESSAGE);
    } else if ((logoffPattern.matcher(input)).matches()) {
      sendDisconnectMessage();
    } else if ((whoPattern.matcher(input)).matches()) {
      sendQueryUsersMessage();
    } else if ((matcher = insultPattern.matcher(input)).matches()) {
      String username = matcher.group(1);
      sendSendInsultMessage(username);
    } else if ((matcher = broadcastPattern.matcher(input)).matches()) {
      String message = matcher.group(1);
      if (message.isEmpty()) {
        chatUI.showErrorMessageWithHelp(EMPTY_MESSAGE_ERROR);
        return;
      }
      sendBroadcastMessage(message);
    } else if ((matcher = directMessagePattern.matcher(input)).find()) {
      String recipient = matcher.group(1);
      String message = matcher.group(2);
      if (message.isEmpty()) {
        chatUI.showErrorMessageWithHelp(EMPTY_MESSAGE_ERROR);
        return;
      }
      sendDirectMessage(recipient, message);
    } else {
      chatUI.showErrorMessageWithHelp(UNKNOWN_COMMAND_ERROR);
    }
  }

  private void sendSendInsultMessage(String recipientUsername) {
    try {
      SendInsultMessage sendInsultMessage = new SendInsultMessage(username, recipientUsername);
      sendInsultMessage.sendToStream(out);
    } catch (IOException e) {
      chatUI.showErrorMessage(INSULT_SEND_FAILED_ERROR);
    }
  }

  private void sendQueryUsersMessage() {
    try {
      QueryUsersMessage queryUsersMessage = new QueryUsersMessage(username);
      queryUsersMessage.sendToStream(out);
    } catch (IOException e) {
      chatUI.showErrorMessage(QUERY_USERS_FAILED_ERROR);
    }
  }

  private void sendBroadcastMessage(String message) {
    try {
      BroadcastMessage broadcastMessage = new BroadcastMessage(username, message);
      broadcastMessage.sendToStream(out);
    } catch (IOException e) {
      chatUI.showErrorMessage(BROADCAST_SEND_ERROR);
    }
  }

  private void sendDirectMessage(String recipient, String message) {
    try {
      DirectMessage directMessage = new DirectMessage(username, recipient, message);
      directMessage.sendToStream(out);
    } catch (IOException e) {
      chatUI.showErrorMessage(DIRECT_SEND_ERROR);
    }
  }

  private void sendDisconnectMessage() {
    try {
      DisconnectMessage disconnectMessage = new DisconnectMessage(username);
      disconnectMessage.sendToStream(out);
    } catch (IOException e) {
      chatUI.showErrorMessage(DISCONNECT_FAILED_ERROR);
    }
  }

  /**
   * indicate the connection for the user
   *
   * @param username username
   * @throws IOException for exception
   */
  public void connectToChat(String username) throws IOException {
    this.username = username;
    // Send ConnectMessage to the server
    new ConnectMessage(username).sendToStream(out);
    // Wait for server response
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatMessageHandler that = (ChatMessageHandler) o;
    return Objects.equals(out, that.out) && Objects.equals(chatUI, that.chatUI)
        && Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(out, chatUI, username);
  }

  @Override
  public String toString() {
    return "ChatMessageHandler{" +
        "out=" + out +
        ", chatUI=" + chatUI +
        ", username='" + username + '\'' +
        '}';
  }
}
