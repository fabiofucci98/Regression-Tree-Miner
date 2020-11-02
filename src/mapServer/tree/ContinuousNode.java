package tree;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to model the abstraction of the entity "split node", relative to
 * an independent continuous attribute.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
class ContinuousNode extends SplitNode {
  /**
   * Initializes a ContinuousNode by calling the super constructor.
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
  ContinuousNode(Data trainingSet, int beginExampleIndex, int endExampleIndex,
      ContinuousAttribute attribute) throws NoBestExplanatoryValueFoundException {
    super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
  }

  /**
   * Method implemented from the abstract super class SplitNode, instantiates
   * SplitInfo objects with each of the possible continuous values that the
   * attribute might assume in the given subset.
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
  void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex,
      Attribute attribute) throws NoBestExplanatoryValueFoundException {
    Double currentSplitValue = (Double) trainingSet.getExplanatoryValue(beginExampleIndex,
        attribute.getIndex());
    double bestInfoVariance = 0;
    List<SplitInfo> bestMapSplit = null;

    for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
      Double value = (Double) trainingSet.getExplanatoryValue(i, attribute.getIndex());
      if (value.doubleValue() != currentSplitValue.doubleValue()) {
        double localVariance = new LeafNode(trainingSet, beginExampleIndex, i - 1).getVariance();
        double candidateSplitVariance = localVariance;
        localVariance = new LeafNode(trainingSet, i, endExampleIndex).getVariance();
        candidateSplitVariance += localVariance;
        if (bestMapSplit == null) {
          bestMapSplit = new ArrayList<SplitInfo>();
          bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
          bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
          bestInfoVariance = candidateSplitVariance;
        } else {

          if (candidateSplitVariance < bestInfoVariance) {
            bestInfoVariance = candidateSplitVariance;
            bestMapSplit.set(0,
                new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
            bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
          }
        }
        currentSplitValue = value;
      }
    }
    if (bestMapSplit == null) {
      throw new NoBestExplanatoryValueFoundException();
    }
    setMapSplit(bestMapSplit);
    if ((getMapSplit().get(1).getBeginIndex() == getMapSplit().get(1).getEndIndex())) {
      getMapSplit().remove(1);

    }
  }

  /**
   * Method implemented from the abstract super class SplitNode, compares the
   * value of the continuous attribute taken as input with each of the splitValue
   * of the SplitInfo inside the ArrayList mapSplit. If it is equal to one of
   * them, returns its index in the list, else returns -1.
   * 
   * @param value Value of the continuous attribute that we want to find.
   * @return Index of the SplitInfo associated with value inside mapSplit.
   */
  int testCondition(Object value) {
    for (SplitInfo splitInfo : getMapSplit()) {
      if (splitInfo.getSplitValue().equals(value)) {
        return getMapSplit().indexOf(splitInfo);
      }
    }
    return -1;
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return "CONTINUOUS " + super.toString();
  }
}