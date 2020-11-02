package database;

/**
 * Thrown if it's impossible to connect to the database.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {
  DatabaseConnectionException() {
    super();
  }

  DatabaseConnectionException(Throwable cause) {
    super(cause);
  }
}
