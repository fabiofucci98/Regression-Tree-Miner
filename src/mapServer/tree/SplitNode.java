package tree;

import data.Attribute;
import data.Data;
import java.io.Serializable;
import java.util.List;

/**
 * Abstract class used to model the abstraction of the entity "split node"
 * (continuous or discrete).
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
abstract class SplitNode extends Node implements Comparable<SplitNode> {
  /**
   * Aggregates all the info about a specific split node.
   * 
   * @author Fabio
   *
   */

  class SplitInfo implements Serializable {
    /**
     * Split value of this info.
     */
    private final Object splitValue;
    /**
     * Begin example indec in the training set.
     */
    private final int beginIndex;
    /**
     * End example index in the training set.
     */
    private final int endIndex;
    /**
     * Index in the array mapSplit of this SplitInfo.
     */
    private final int numberChild;
    /**
     * Comparator used.
     */
    private String comparator = "=";

    /**
     * Initializes the class attributes for a discrete node.
     * 
     * 
     * @param splitValue  Represents the independent attribute that defines the
     *                    split.
     * @param beginIndex  Index of the first example of the training set covered by
     *                    the node.
     * @param endIndex    Index of the last example of the training set covered by
     *                    the node.
     * @param numberChild Number of children that the node has in the regression
     *                    tree.
     */
    SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
      this.splitValue = splitValue;
      this.beginIndex = beginIndex;
      this.endIndex = endIndex;
      this.numberChild = numberChild;
    }

    /**
     * Initializes the class attributes for a continuous node.
     * 
     * @param splitValue  Represents the independent attribute that defines the
     *                    split.
     * @param beginIndex  Index of the first example of the training set covered by
     *                    the node.
     * @param endIndex    Index of the last example of the training set covered by
     *                    the node.
     * @param numberChild Number of children that the node has in the regression
     *                    tree.
     * @param comparator  Mathematical operator that defines the test for the
     *                    current node.
     */
    SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
      this.splitValue = splitValue;
      this.beginIndex = beginIndex;
      this.endIndex = endIndex;
      this.numberChild = numberChild;
      this.comparator = comparator;
    }

    /**
     * Returns the index of the first example of the training set covered by the
     * node.
     * 
     * @return Value of beginIndex.
     */
    int getBeginIndex() {
      return beginIndex;
    }

    /**
     * Returns the index of the last example of the training set covered by the
     * node.
     * 
     * @return Value of endIndex.
     */
    int getEndIndex() {
      return endIndex;
    }

    /**
     * Returns the value of the independent attribute that defines the split.
     * 
     * @return Value of splitValue.
     */
    Object getSplitValue() {
      return splitValue;
    }

    /**
     * Returns the comparator defined for the test.
     * 
     * @return Value of the mathematical operator defined for the test.
     */
    String getComparator() {
      return comparator;
    }

    public String toString() {
      return "child " + numberChild + " split value" + comparator + splitValue + "[Examples:"
          + beginIndex + "-" + endIndex + "]";
    }

  }

  /**
   * Attribute that defines the SplitNode.
   */
  private final Attribute attribute;
  /**
   * List of split info containing all the info computed.
   */
  private List<SplitInfo> mapSplit;
  /**
   * Variance of the split node.
   */
  private final double splitVariance;
  /**
   * Constant used for comparision between floating point numbers.
   */
  private static final double EPSILON = 0.00001;

  /**
   * Orders the values of the input attribute in the interval
   * [beginExampleIndex-endxampleIndex] e uses this ordering to determine the
   * possible SplitInfo, which will be stored inside the ArrayList mapSplit.
   * Finally computes the SSE as the sum of the SSEs computed for each split.
   * 
   * 
   * @param trainingSet       Collection of learning examples.
   * @param beginExampleIndex Index of the first example of the training set
   *                          covered by the node.
   * @param endExampleIndex   Index of the last example of the training set
   *                          covered by the node.
   * @param attribute         Independent attribute that defines the split.
   * @throws NoBestExplanatoryValueFoundException When the node is defined on a
   *                                              subset of the training set of
   *                                              length zero or less
   *                                              (endExampleIndex -
   *                                              beginExampleIndex &lt;= 0 )
   */
  SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute)
      throws NoBestExplanatoryValueFoundException {
    super(trainingSet, beginExampleIndex, endExampleIndex);
    double tempSplitVariance = 0;
    this.attribute = attribute;
    trainingSet.sort(attribute, beginExampleIndex, endExampleIndex);
    setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    for (SplitInfo splitInfo : getMapSplit()) {
      double localVariance = new LeafNode(trainingSet, splitInfo.getBeginIndex(),
          splitInfo.getEndIndex()).getVariance();
      tempSplitVariance += (localVariance);
    }
    splitVariance = tempSplitVariance;
  }

  /**
   * Abstract method to generate the info for each of the candidate splits.
   * 
   * @param trainingSet       Collection of learning examples.
   * @param beginExampleIndex Index of the first example of the training set
   *                          covered by the node.
   * @param endExampleIndex   Index of the last example of the training set
   *                          covered by the node.
   * @param attribute         Independent attribute that defines the split.
   * @throws NoBestExplanatoryValueFoundException When the node is defined on a
   *                                              subset of the training set of
   *                                              length zero or less
   *                                              (endExampleIndex -
   *                                              beginExampleIndex &lt;= 0 )
   */
  abstract void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex,
      Attribute attribute) throws NoBestExplanatoryValueFoundException;

  /**
   * Abstract method to model the test condition. Compares the input attribute
   * against the ones contained in mapSplit and returns the identifier of the
   * split on which the test is positive.
   * 
   * @param value value of the attribute that has to be tested against all the
   *              other splits.
   * @return number of the split branch.
   */
  abstract int testCondition(Object value);

  /**
   * Returns the independent attribute that defines the split.
   * 
   * @return Value of attribute.
   */
  Attribute getAttribute() {
    return attribute;
  }

  @Override
  double getVariance() {
    return splitVariance;
  }

  /**
   * Implementation from the abstract class Node, returns the number of children
   * of the SplitNode.
   * 
   * @return the size of the list mapSplit.
   */
  int getNumberOfChildren() {
    return getMapSplit().size();
  }

  /**
   * Returns the info for the branch indexed by child.
   * 
   * @param child Index of the children we are interested in.
   * @return Value of mapSplit at index child.
   */
  SplitInfo getSplitInfo(int child) {
    return getMapSplit().get(child);
  }

  /**
   * Returns the list of SplitInfo computed for the current node.
   * 
   * @return Value of mapSplit.
   */
  List<SplitInfo> getMapSplit() {
    return mapSplit;
  }

  /**
   * Sets the list of SplitInfo for the current node.
   * 
   * @param mapSplit List of SplitInfo to assign to mapSplit.
   */
  void setMapSplit(List<SplitInfo> mapSplit) {
    this.mapSplit = mapSplit;
  }

  /**
   * Generates a String with all the split info.
   * 
   * @return A String obtained by concatenating all the info of every split.
   */
  String formulateQuery() {
    String query = "";
    int i = 0;
    for (SplitInfo splitInfo : getMapSplit()) {
      query += (i + ":" + attribute + splitInfo.getComparator() + splitInfo.getSplitValue()) + "\n";
      i++;
    }
    return query;
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    String v = "SPLIT : attribute=" + attribute + " " + super.toString() + " Split Variance: "
        + getVariance() + "\n";
    for (SplitInfo splitInfo : getMapSplit()) {
      v += "\t" + splitInfo + "\n";
    }

    return v;
  }

  /**
   * Compares this SplitNode with o.
   * 
   * @param o SplitNode to compare to this object.
   */
  public int compareTo(SplitNode o) {
    if (o == this) {
      return 0;
    }
    SplitNode splitNode = (SplitNode) o;
    if (Math.abs((splitVariance - splitNode.getVariance())) < EPSILON) {
      return 0;
    } else {
      return splitVariance < splitNode.getVariance() ? -1 : 1;
    }
  }

}