package data;

/**
 * Custom exception used to handle an erroneous acquisition of the training set
 * from the database.In particular, when the table does not exist, when the
 * training set is empty or without a numerical target variable.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class TrainingDataException extends Exception {

  public TrainingDataException(String msg) {
    super(msg);
  }

  public TrainingDataException(Throwable cause) {
    super(cause);
  }

}
