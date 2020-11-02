package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Instantiates a ServerOneClient for every communication request.
 * 
 * @author Fabio
 *
 */
public class MultiServer {

  /**
   * port used to connect to the server.
   */
  private final int port;

  /**
   * Initializes the attribute port.
   * 
   * @param port port used to connect to the server.
   */
  public MultiServer(int port) {
    this.port = port;
    run();
  }

  /**
   * Instantiates a ServerSocket which will wait for a connection request from the
   * client. For every request instantiates a new ServerOneClient
   */
  @SuppressWarnings("resource")
  private void run() {
    ServerSocket serverSocket;
    Socket clientSocket;
    ServerOneClient clientThread;
    System.out.println("Welcome! Waiting for a connection");
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.err
          .println("There is already a server running at port " + port + ". Closing the software.");
      return;
    }
    while (true) {
      try {
        clientSocket = serverSocket.accept();
        clientThread = new ServerOneClient(clientSocket);
        clientThread.start();
      } catch (IOException e) {
        System.err
            .println("There was un unexpected problem while instantiating the thread: " + e + "\n");
      }
    }
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return "" + port;
  }

}
