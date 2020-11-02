package database;

/**
 * Thrown if the database returns an empty set when queried.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class EmptySetException extends Exception {
  EmptySetException() {
    super();
  }
}
