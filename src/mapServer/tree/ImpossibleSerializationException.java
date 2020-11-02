package tree;

/**
 * Thrown if there are serialization problem while saving/loading the tree.
 * 
 * @author fabio
 *
 */
@SuppressWarnings("serial")
public class ImpossibleSerializationException extends Exception {

  ImpossibleSerializationException(String msg) {
    super(msg);
  }

  ImpossibleSerializationException(Throwable cause) {
    super(cause);
  }

}
