import server.MultiServer;

/**
 * Software main class.
 * 
 * @author fabio
 *
 */
public class MainTest {

  private static final int PORT = 8080;

  /**
   * Software entry point, initializes a new MultiServer on standard port 8080.
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) {
    new MultiServer(PORT);
  }

}
