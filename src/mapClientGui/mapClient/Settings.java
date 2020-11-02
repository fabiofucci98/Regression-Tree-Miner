package mapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class used to contain all the settings of the software.
 * 
 * @author Fabio
 *
 */
public class Settings {
  /**
   * Enumaration containing the values Data and File.
   * 
   * @author Fabio
   *
   */
  enum LearnFrom {
    Data, File
  }

  /**
   * String contaning the server port.
   */
  private String port = "8080";

  /**
   * String containing the server ip.
   */
  private String ip = "localhost";
  /**
   * String containing the name of the table from which the regression tree will
   * be created.
   */
  private String tableName = "";
  /**
   * Attribute used to indicate if the user wants to learn from table or load from
   * file.
   */
  private LearnFrom getTreeFrom = LearnFrom.Data;
  /**
   * Socket used to communicate with the server.
   */
  private Socket socket;
  /**
   * Output stream used to communicate with the server.
   */
  private ObjectOutputStream out;
  /**
   * Input stream used to communicate with the server.
   */
  private ObjectInputStream in;
  /**
   * Attribute used to know if the client is connected.
   */
  private boolean connected = false;
  /**
   * Attribute used to know if it's the first time the client tries to connect.
   */
  private boolean firstConnection = true;

  String getPort() {
    return port;
  }

  void setPort(String port) {
    this.port = port;
  }

  String getIp() {
    return ip;
  }

  void setIp(String ip) {
    this.ip = ip;
  }

  String getTableName() {
    return tableName;
  }

  void setTableName(String tableName) {
    this.tableName = tableName;
  }

  Socket getSocket() {
    return socket;
  }

  void setSocket(Socket socket) {
    this.socket = socket;
  }

  ObjectOutputStream getOut() {
    return out;
  }

  void setOut(ObjectOutputStream out) {
    this.out = out;
  }

  ObjectInputStream getIn() {
    return in;
  }

  void setIn(ObjectInputStream in) {
    this.in = in;
  }

  boolean isConnected() {
    return connected;
  }

  void setConnected(boolean connected) {
    this.connected = connected;
  }

  boolean isFirstConnection() {
    return firstConnection;
  }

  void setFirstConnection(boolean firstConnection) {
    this.firstConnection = firstConnection;
  }

  LearnFrom getGetTreeFrom() {
    return getTreeFrom;
  }

  void setGetTreeFrom(LearnFrom getTreeFrom) {
    this.getTreeFrom = getTreeFrom;
  }

  /**
   * Method used to close the connection with the server.
   */
  public void closeConnection() {
    try {
      out.writeObject(0);
      out.close();
      in.close();
      socket.close();
      connected = false;
    } catch (IOException | NullPointerException e) {

    }
  }
}
