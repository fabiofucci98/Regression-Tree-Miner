package server;

import data.Data;
import data.TrainingDataException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import tree.ImpossibleSerializationException;
import tree.RegressionTree;

/**
 * Class used to handle the various client communication requests.
 * 
 * @author Fabio
 *
 */
class ServerOneClient extends Thread {
  /**
   * Socket used to communicate with the client.
   */
  private final Socket socket;
  /**
   * Input stream used to communicate with the client.
   */
  private final ObjectInputStream in;
  /**
   * Output stream used to communicate with the client.
   */
  private final ObjectOutputStream out;
  /**
   * Used to count how many clients are connected.
   */
  private static int threadCount = 0;
  /**
   * Id of the connected client.
   */
  private final int threadNumber;

  /**
   * Initializes the class attributes and starts the thread.
   * 
   * @param s Connection socket, used to open communication streams.
   * @throws IOException If there are problems getting the I/O strems from the
   *                     socket.
   */
  public ServerOneClient(Socket s) throws IOException {
    threadNumber = threadCount;
    threadCount++;
    socket = s;
    in = new ObjectInputStream(socket.getInputStream());
    out = new ObjectOutputStream(socket.getOutputStream());
  }

  /**
   * Overrides the method run() of class Thread.
   */
  @Override
  public void run() {
    System.out.println("Starting thread number [" + threadNumber + "]");
    RegressionTree tree = null;
    String tableName;
    Data data = null;
    int phase;
    try {
      phase = (int) in.readObject();
      if (phase == 0) {
        System.out.println("Thread [" + threadNumber + "] is starting learning phase");
        tableName = in.readObject().toString();
        data = new Data(tableName);
        tree = new RegressionTree(data);

        out.writeObject("OK");
        phase = (int) in.readObject();
        if (phase != 1) {
          System.out.println(
              "Thread [" + threadNumber + "] did not specify a correct phase, closing the thread.");
          return;
        }
        System.out.println("Thread [" + threadNumber + "] is saving the tree");
        tree.save(tableName + ".dmp");
        out.writeObject("OK");

      } else if (phase == 2) {
        System.out.println("Thread [" + threadNumber + "] is starting loading phase");
        tableName = in.readObject().toString();

        tree = RegressionTree.load(tableName + ".dmp");

        out.writeObject("OK");
      } else {
        System.out.println(
            "Thread [" + threadNumber + "] did not specify a correct phase, closing the thread.");
        return;
      }

      phase = (int) in.readObject();
      if (phase == 3) {
        System.out.println("Thread [" + threadNumber + "] is starting prediction phase");

      }
      while (phase == 3) {
        try {

          Double predictedValue = tree.predictClass(in, out);

          out.writeObject(predictedValue);
        } catch (UnknownValueException e) {
          out.writeObject(e.getMessage());
        }
        phase = (int) in.readObject();

      }

      System.out.println("Closing thread number [" + threadNumber + "]");

    } catch (ClassNotFoundException | IOException | TrainingDataException
        | ImpossibleSerializationException e) {
      try {
        out.writeObject(e.getMessage());
      } catch (IOException e1) {
      }
      System.err.println(e.toString());
      System.err.println("Closing thread number [" + threadNumber + "]");
    }
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return "" + socket + " " + in + " " + out;
  }
}
