package tree;

import data.Data;

/**
 * Models the abstraction "leaf node".
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
class LeafNode extends Node {
/**
 * Class value predicted by this leaf node as the avarage between the class values in the subset of the training set covered by this node.
 */
  private final double predictedClassValue;

  /**
   * Instantiates a LeafNode by calling the super constructor and initializes the
   * attribute predictedClassValue as the average between the class values that
   * fall in the interval [beginExampleIndex,endExampleIndex].
   * 
   * @param trainingSet       Collection of learning examples.
   * @param beginExampleIndex Index of the first example of the training set
   *                          covered by the node.
   * @param endExampleIndex   Index of the last example of the training set
   *                          covered by the node.
   */
  LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
    super(trainingSet, beginExampleIndex, endExampleIndex);
    int numberOfExamples = endExampleIndex - beginExampleIndex;
    double sum = 0;
    for (int i = beginExampleIndex; i < endExampleIndex; i++) {
      sum += trainingSet.getClassValue(i);
    }
    predictedClassValue = sum / numberOfExamples;
  }

  /**
   * Returns the predicted class value.
   * 
   * @return Value of the attribute predictedClassValue.
   */
  double getPredictedClassValue() {
    return predictedClassValue;
  }

  /**
   * Implementation from the abstract class Node, returns the number of children
   * of any LeafNode, that is, 0.
   * 
   * @return Number of children of a LeafNode, 0.
   */
  int getNumberOfChildren() {
    return 0;
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return "LEAF : class=" + predictedClassValue + " " + super.toString();
  }

}