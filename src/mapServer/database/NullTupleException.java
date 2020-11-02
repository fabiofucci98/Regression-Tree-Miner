package database;

/**
 * Thrown if a tuple containing null values is found in the table.
 * 
 * @author Fabio
 *
 */
public class NullTupleException extends Exception {

  private static final long serialVersionUID = 1L;

  public NullTupleException() {
    super();
  }
}
