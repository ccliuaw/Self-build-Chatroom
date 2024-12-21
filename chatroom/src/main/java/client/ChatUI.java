package client;

import static client.ChatMessageHandler.HELP_COMMAND;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;
import protocol.BroadcastMessage;
import protocol.ConnectResponse;
import protocol.DirectMessage;
import protocol.FailedMessage;
import protocol.QueryUsersResponse;

/**
 * Represent the UI for the client and handle the user input
 */
public class ChatUI {

  /**
   * Default host for server connection.
   */
  public static final String DEFAULT_HOST = "localhost";
  /**
   * Suffix for input prompts.
   */
  public static final String INPUT_PROMPT_SUFFIX = ": > ";
  /**
   * Default port for server connection.
   */
  public static final String DEFAULT_PORT = "12345";
  /**
   * Prompt for getting server host.
   */
  public static final String GET_HOST_PROMPT =
      "Enter IP address of server to connect to (default: " + DEFAULT_HOST + ")"
          + INPUT_PROMPT_SUFFIX;
  /**
   * Prompt for getting server port.
   */
  public static final String GET_PORT_PROMPT =
      "Enter Server Port (default: " + DEFAULT_PORT + ")" + INPUT_PROMPT_SUFFIX;
  /**
   * Prompt for entering username.
   */
  public static final String USERNAME_PROMPT = "Enter username" + INPUT_PROMPT_SUFFIX;
  /**
   * Represents an empty string.
   */
  public static final String EMPTY_STRING = "";
  /**
   * Welcome message for successful connection.
   */
  public static final String WELCOME_MESSAGE = "Connected to the chat!";
  /**
   * General instructions for chat usage.
   */
  public static final String GENERAL_INSTRUCTIONS = "Enter messages or type \"?\" for help.";
  /**
   * Represents a space character.
   */
  public static final String SPACE = " ";
  /**
   * Suffix for help instruction.
   */
  public static final String HELP_SUFFIX = "Use " + HELP_COMMAND + " for the help instructions";
  /**
   * Message for successful connection.
   */
  public static final String SUCCESSFULLY_CONNECTED_MESSAGE = "Successfully Connected";
  /**
   * Prefix for broadcast messages.
   */
  public static final String BROADCAST_PREFIX = System.lineSeparator() + "(broadcast) ";
  /**
   * Prefix for private messages.
   */
  public static final String PRIVATE_PREFIX = System.lineSeparator() + "(private) ";
  /**
   * Separator for username and message.
   */
  public static final String COLON_SEPARATOR = ": ";
  /**
   * Prefix for server messages.
   */
  public static final String SERVER_MESSAGE_PREFIX = System.lineSeparator() + "SERVER: ";
  /**
   * Prefix for server error messages.
   */
  public static final String SERVER_ERROR_PREFIX = System.lineSeparator() + "SERVER ERROR: ";
  /**
   * Message displayed when exiting the chat.
   */
  public static final String EXITING_MESSAGE = "Exiting...";
  /**
   * Message when no other users are connected.
   */
  public static final String NO_OTHER_CONNECTED_USERS_MESSAGE = "No other connected users";
  /**
   * Prefix for listing connected users.
   */
  public static final String CONNECTED_USERS_PREFIX = "Connected Users: ";
  /**
   * Separator for listing multiple items.
   */
  public static final String COMMA_SEPARATOR = ", ";
  /**
   * Message displayed when chat is disconnected.
   */
  public static final String CHAT_DISCONNECTED = "Chat disconnected. Enter a non-empty message to exit.";

  private final Scanner scanner;
  private PrintStream outStream;
  private PrintStream errorStream;
  private InputStream inputStream;

  /**
   * Construct a ChatUI instance
   *
   * @param inputStream input stream
   */
  public ChatUI(InputStream inputStream) {
    this.scanner = new Scanner(inputStream);
    this.outStream = System.out;
    this.errorStream = System.err;
    this.inputStream = inputStream;
  }

  /**
   * Construct a ChatUI instance
   *
   * @param scanner   scanner instance
   * @param outStream the out stream
   * @param errStream the err stream
   */
  public ChatUI(Scanner scanner, PrintStream outStream, PrintStream errStream) {
    this.scanner = scanner;
    this.outStream = outStream;
    this.errorStream = errStream;
  }

  /**
   * ask user to input the host IP address or host name
   *
   * @return host IP address or host name
   */
  public String getHostFromUser() {
    showMessageSameLine(GET_HOST_PROMPT);
    return getValueFromUserWithDefault(DEFAULT_HOST);
  }

  /**
   * ask user to input the port name
   *
   * @return port name
   */
  public String getPortFromUser() {
    showMessageSameLine(GET_PORT_PROMPT);
    return getValueFromUserWithDefault(DEFAULT_PORT);
  }

  /**
   * ask user to input username
   *
   * @return username string
   */
  public String login() {
    return getNonEmptyValueFromUser(USERNAME_PROMPT);
  }

  /**
   * ask user to input messages
   *
   * @param username user name
   * @return string for user's input block
   */
  public String getChatBoxInput(String username) {
    return getNonEmptyValueFromUser(username + INPUT_PROMPT_SUFFIX);
  }

  private String getValueFromUserWithDefault(String defaultValue) {
    String value = scanner.nextLine();

    if (value.isEmpty()) {
      value = defaultValue;
    }
    return value;
  }

  private String getNonEmptyValueFromUser(String prompt) {
    String input = EMPTY_STRING;
    while (input.isEmpty()) {
      showMessageSameLine(prompt);
      input = scanner.nextLine().trim();
    }
    return input;
  }

  /**
   * display Welcome To Chat Message
   */
  public void displayWelcomeToChatMessage() {
    showMessage(WELCOME_MESSAGE);
    showMessage(GENERAL_INSTRUCTIONS);
  }

  /**
   * show message
   *
   * @param message input message
   */
  public void showMessage(String message) {
    outStream.println(message);
  }

  /**
   * show message in same line
   *
   * @param message input message
   */
  public void showMessageSameLine(String message) {
    outStream.print(message);
  }

  /**
   * show the error message
   *
   * @param errorMessage input message
   */
  public void showErrorMessage(String errorMessage) {
    errorStream.println(errorMessage);
  }

  /**
   * show the error message with Help
   *
   * @param errorMessage input message
   */
  public void showErrorMessageWithHelp(String errorMessage) {
    errorStream.println(errorMessage + SPACE + HELP_SUFFIX);
  }

  /**
   * show connected to server message
   */
  public void showConnectedMessage() {
    showMessage(SUCCESSFULLY_CONNECTED_MESSAGE);
  }

  /**
   * show received message from other user
   *
   * @param from    other user
   * @param message message context
   * @param to      recipient
   */
  public void showReceivedMessage(String from, String message, String to) {
    if (to.isEmpty()) {
      showMessage(BROADCAST_PREFIX + from + COLON_SEPARATOR + message);
    } else {
      showMessage(PRIVATE_PREFIX + from + COLON_SEPARATOR + message);
    }
  }

  /**
   * show received message from other user
   *
   * @param from    other user
   * @param message message context
   */
  public void showReceivedMessage(String from, String message) {
    showReceivedMessage(from, message, EMPTY_STRING);
  }

  /**
   * show message from server
   *
   * @param message message context
   */
  public void showServerMessage(String message) {
    showMessage(SERVER_MESSAGE_PREFIX + message);
  }

  /**
   * show error message from server
   *
   * @param message message context
   */
  public void showServerError(String message) {
    showErrorMessage(SERVER_ERROR_PREFIX + message);
  }

  /**
   * close the scanner
   */
  public void cleanup() {
    showMessage(EXITING_MESSAGE);
    scanner.close();
  }

  /**
   * show the broadcast message
   *
   * @param broadcast input BroadcastMessage
   */
  public void showBroadcastMessage(BroadcastMessage broadcast) {
    showReceivedMessage(broadcast.getSenderUsername(), broadcast.getMessage());
  }

  /**
   * show the direct message
   *
   * @param direct input DirectMessage
   */
  public void showDirectMessage(DirectMessage direct) {
    showReceivedMessage(direct.getSenderUsername(), direct.getMessage(),
        direct.getRecipientUsername());
  }

  /**
   * show the failed message
   *
   * @param failed input FailedMessage
   */
  public void showFailedMessage(FailedMessage failed) {
    showServerError(failed.getMessage());
  }

  /**
   * show the query users response message
   *
   * @param queryResponse input QueryUsersResponse
   */
  public void showQueryUsersResponse(QueryUsersResponse queryResponse) {
    if (queryResponse.getConnectedUsers().isEmpty()) {
      showServerMessage(NO_OTHER_CONNECTED_USERS_MESSAGE);
      return;
    }
    showServerMessage(
        CONNECTED_USERS_PREFIX + String.join(COMMA_SEPARATOR, queryResponse.getConnectedUsers()));
  }

  /**
   * show disconnect response message
   *
   * @param disconnectResponse input DisconnectResponse
   */
  public void showDisconnectResponse(ConnectResponse disconnectResponse) {
    showServerMessage(disconnectResponse.getMessage());
    showMessage(CHAT_DISCONNECTED);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatUI chatUI = (ChatUI) o;
    return Objects.equals(outStream,
        chatUI.outStream) && Objects.equals(errorStream, chatUI.errorStream)
        && Objects.equals(inputStream, chatUI.inputStream);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outStream, errorStream, inputStream);
  }

  @Override
  public String toString() {
    return "ChatUI{" +
        ", outStream=" + outStream +
        ", errorStream=" + errorStream +
        ", inputStream=" + inputStream +
        '}';
  }
}
