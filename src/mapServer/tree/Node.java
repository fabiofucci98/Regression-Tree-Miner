package tree;

import data.Data;
import java.io.Serializable;

/**
 * Abstract class used to model a node (intermediate or leaf) of the decision
 * tree.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
abstract class Node implements Serializable {
  /**
   * Used to know how many nodes are created.
   */
  private static int idNodeCount = 0;
  /**
   * Id of this node.
   */
  private final int idNode;
  /**
   * Begin example index covered by the node in the training set.
   */
  private final int beginExampleIndex;
  /**
   * End example index covered by the node in the training set.
   */
  private final int endExampleIndex;
  /**
   * Variance computed for this node.
   */
  private final double variance;

  /**
   * Assigns the inputs to the class fields beginExampleIndex and endExampleIndex,
   * Increases the node count and generates the node id. Lastly, calculates the
   * SSE (Sum of Squared Errors) of the class attribute to predict in the subset
   * of trainingSet covered by the node.
   * 
   * @param trainingSet       Object of class Data containing the training set.
   * @param beginExampleIndex Index of the first example covered by the node.
   * @param endExampleIndex   Index of the last example covered by the node.
   */
  Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
    double sumOfSquares = 0;
    double squaresOfSum = 0;

    idNode = idNodeCount;
    idNodeCount++;

    this.beginExampleIndex = beginExampleIndex;
    this.endExampleIndex = endExampleIndex;

    for (int i = beginExampleIndex; i <= endExampleIndex; i++) {
      squaresOfSum += trainingSet.getClassValue(i);
      sumOfSquares += Math.pow(trainingSet.getClassValue(i), 2);
    }
    int numberOfExamples = endExampleIndex - beginExampleIndex + 1;

    variance = sumOfSquares - Math.pow(squaresOfSum, 2) / numberOfExamples;
  }

  /**
   * Returns the numeric identifier of the node.
   * 
   * @return Value of idNode
   */
  int getIdNode() {
    return idNode;
  }

  /**
   * Returns the index of the first example of the subset covered by the node.
   * 
   * @return Value of beginExampleIndex
   */
  int getBeginExampleIndex() {
    return beginExampleIndex;
  }

  /**
   * Returns the index of the last example of the subset covered by the node.
   * 
   * @return Value of endExampleIndex
   */
  int getEndExampleIndex() {
    return endExampleIndex;
  }

  /**
   * Returns the value of the SSE computed on the subset of examples covered by
   * the node.
   * 
   * @return Value of variance.
   */
  double getVariance() {
    return variance;
  }

  /**
   * Abstract method implemented by SplitNode and LeafNode.
   * 
   * @return Number of children the node has in the regression tree.
   */
  abstract int getNumberOfChildren();

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return "Nodo: [Examples:" + beginExampleIndex + "-" + endExampleIndex + "] variance:"
        + variance;
  }
}