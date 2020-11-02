package mapClient;

/**
 * Thrown if there are problems connecting to the server.
 * 
 * @author Fabio
 *
 */
public class NotConnectedException extends Exception {

  private static final long serialVersionUID = -5329440756726089943L;

  public NotConnectedException() {
    super();
  }

  public NotConnectedException(String msg) {
    super(msg);
  }
}
