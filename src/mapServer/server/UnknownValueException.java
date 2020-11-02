package server;

/**
 * This exception is thrown by the method predictClass() in the class
 * RegressionTree. In the prediction phase, the method prompts the user to
 * choose what branch of the tree to follow in order to obtain the predicted
 * class value, but if the user inputs a non numerical or out of range character
 * the exception is thrown.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class UnknownValueException extends Exception {
  public UnknownValueException(String msg) {
    super(msg);
  }
}
