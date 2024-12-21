## Chat Room Application

This README provides an overview of the Chat Room application, including how to run the program, key
classes and methods, assumptions made, and steps taken to ensure correctness.

## How to Run the Program

## Server

To run the server:

1. Navigate to the `server` package.
2. Run the `ServerLauncher` class.

The server will start and display the port it's listening on.

## Client

To run the client:

1. Navigate to the `client` package.
2. Run the `ChatLauncher` class.

The client will prompt you to enter the server's IP address and port number.

## Key Classes and Methods

## Server-side

- `Server`: Main server class that accepts client connections.`start()`: Begins listening for client
  connections.
- `ClientManager`: Manages individual client connections.`run()`: Handles client messages and
  maintains the connection.
- `ClientMessageHandler`: Processes messages received from clients.`handleMessage()`: Interprets and
  acts on different message types.

## Client-side

- `ChatClient`: Main client class that manages the connection to the server.`start()`: Initiates the
  connection and handles user input.
- `ChatMessageHandler`: Processes user input and sends messages to the server.`handleChatInput()`:
  Interprets user commands and sends appropriate messages.
- `ServerMessageProcessor`: Handles incoming messages from the server.`run()`: Continuously listens
  for and processes server messages.

## Protocol

- `Message`: Abstract base class for all message types.
- Specific message classes (e.g., `ConnectMessage`, `BroadcastMessage`, etc.) implement the
  protocol.

## Assumptions

1. The server can handle up to 10 concurrent client connections.
2. Clients will provide valid IP addresses and port numbers for connection.
3. Usernames are unique within the chat room.
4. Messages and usernames are encoded in UTF-8.

## Steps to Ensure Correctness

1. Implemented robust error handling and input validation.
2. Used thread-safe collections for managing client connections.
3. Ensured proper closing of resources (sockets, streams) to prevent memory leaks.
4. Implemented the protocol strictly according to the specifications.
5. Tested with multiple concurrent clients to verify multi-threading functionality.

## Command Line Usage

## Server

```
java -cp <path-to-compiled-classes> server.ServerLauncher
```

## Client

```
java -cp <path-to-compiled-classes> client.ChatLauncher
```

## Available Commands

- `?`: Display help menu
- `logoff`: Disconnect from the chat room
- `who`: List connected users
- `@<username> <message>`: Send a private message
- `@all <message>`: Send a message to all users
- `!<username>`: Send a random insult to a user