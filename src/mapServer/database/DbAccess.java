package database;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class used to connect to the database MapDB.
 * 
 * @author Fabio
 *
 */
public class DbAccess {
  /**
   * String containing the path to the jfbc Driver class.
   */
  private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  /**
   * String containing what dbms is used.
   */
  private static final String DBMS = "jdbc:mysql";
  /**
   * String containing the server ip.
   */
  private static final String SERVER = "localhost";
  /**
   * String containing the name of the database.
   */
  private static final String DATABASE = "MapDB";
  /**
   * String containing the number of the port used for the mysql server
   * connection.
   */
  private static final String PORT = "3306";
  /**
   * String containing the name of the mysql user.
   */
  private static final String USER_ID = "MapUser";
  /**
   * String containing the mysql user password.
   */
  private static final String PASSWORD = "map";
  /**
   * Connection instantiated by the jdbc driver.
   */
  private Connection conn;

  /**
   * Loads the mysql driver and initializes the connection.
   * 
   * @throws DatabaseConnectionException If it's impossible to find, access or
   *                                     instantiate the jdbc driver, or if it's
   *                                     impossible to connect to the database.
   */
  public void initConnection() throws DatabaseConnectionException {

    try {
      Class.forName(DRIVER_CLASS_NAME).getConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      System.out.println("[!] Driver not found: " + e.getMessage());
      throw new DatabaseConnectionException();
    } catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
      System.out.println("[!] Error during the instantiation : " + e.getMessage());
      throw new DatabaseConnectionException();
    } catch (IllegalAccessException e) {
      System.out.println("[!] Cannot access the driver : " + e.getMessage());
      throw new DatabaseConnectionException();
    }
    String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user="
        + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

    try {
      conn = DriverManager.getConnection(connectionString);
    } catch (SQLException e) {
      System.out.println("[!] SQLException: " + e.getMessage());
      System.out.println("[!] SQLState: " + e.getSQLState());
      System.out.println("[!] VendorError: " + e.getErrorCode());
      throw new DatabaseConnectionException();
    }
  }

  /**
   * Returns the connection to the database.
   * 
   * @return The value of the attribute conn.
   */
  public Connection getConnection() {
    return conn;
  }

  /**
   * Closes the connection to the database.
   * 
   * @throws SQLException if the connection doesn't exist or cannot be closed.
   */
  public void closeConnection() throws SQLException {
    conn.close();
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
  }

}
