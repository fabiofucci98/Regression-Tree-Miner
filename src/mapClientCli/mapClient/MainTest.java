package mapClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import utility.Keyboard;

/**
 * Main class of the client software.
 * 
 * @author Fabio
 *
 */
public class MainTest {

  /**
   * Entry point of the client software.
   * 
   * @param args Command line arguments. First argument has to be server address,
   *             second one server port.
   */
  public static void main(String[] args) {
    int port;
    InetAddress addr;
    try {
      port = Integer.parseInt(args[1]);
      addr = InetAddress.getByName(args[0]);
    } catch (UnknownHostException | NumberFormatException e) {
      System.out.println(
          "Command line argument doesn't have a valid port number or address, closing software.");
      waitInput();
      return;
    }
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    try {
      socket = new Socket(addr, port);
      System.out.println(socket);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      System.out.println(e.toString());
      waitInput();
      return;
    }

    String answer;

    int decision;
    do {
      System.out.println("Welcome! press 1 to learn from data, press 2 to load from file!");
      System.out.println("Learn Regression Tree from data [1]");
      System.out.println("Load Regression Tree from archive [2]");
      decision = Keyboard.readInt();
    } while (!(decision == 1) && !(decision == 2));

    String tableName;
    System.out.println("Table name:");
    tableName = Keyboard.readString();
    try {
      if (decision == 1) {
        System.out.println("Starting data acquisition phase!");

        out.writeObject(0);
        out.writeObject(tableName);
        answer = in.readObject().toString();
        if (!answer.equals("OK")) {
          System.out.println(answer);
          waitInput();
          return;
        }

        System.out.println("Starting learning phase!");
        out.writeObject(1);

      } else {
        out.writeObject(2);
        out.writeObject(tableName);

      }

      answer = in.readObject().toString();
      if (!answer.equals("OK")) {
        System.out.println(answer);
        return;
      }

      // .........

      char risp = 'y';

      do {
        out.writeObject(3);

        System.out.println("Starting prediction phase!\n");
        answer = in.readObject().toString();

        while (answer.equals("QUERY")) {
          answer = in.readObject().toString();
          System.out.println(answer);
          int path = Keyboard.readInt();
          out.writeObject(path);
          answer = in.readObject().toString();
        }

        if (answer.equals("OK")) {

          System.out.println("Predicted class:" + in.readObject().toString());

        } else {
          System.out.println(answer);
        }

        System.out.println("Would you repeat ? (y/n)");
        risp = Keyboard.readChar();

      } while (Character.toUpperCase(risp) == 'Y');

    } catch (IOException | ClassNotFoundException e) {
      System.out.println(e.toString());
      waitInput();
    }

    try {
      out.writeObject(-1);
      in.close();
      out.close();
      socket.close();

    } catch (IOException e) {
      System.out.println("There was an error closing the stream, closing software.");
      e.printStackTrace();
      waitInput();

    }
  }

  /**
   * Method used to wait for user input before closing the software.
   */
  private static void waitInput() {
    try {
      System.in.read();
    } catch (IOException e) {
    }
  }

}