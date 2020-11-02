package tree;

/**
 * Exception thrown in the method setSplitInfo of class ContinuousNode. Gets
 * thrown if all the explanatory values for every example in the current sub set
 * are equal.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
class NoBestExplanatoryValueFoundException extends Exception {
  NoBestExplanatoryValueFoundException() {
    super();
  }
}
