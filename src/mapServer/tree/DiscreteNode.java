package tree;

import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import java.util.ArrayList;

/**
 * Class used to model the abstraction of the entity "split node", relative to
 * an independent discrete attribute.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
class DiscreteNode extends SplitNode {
  /**
   * Instantiates a DiscreteNode by calling the super constructor.
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
  DiscreteNode(Data trainingSet, int beginExampleIndex, int endExampleIndex,
      DiscreteAttribute attribute) throws NoBestExplanatoryValueFoundException {
    super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
  }

  /**
   * Method implemented from the abstract super class SplitNode, instantiates
   * SplitInfo objects with each of the possible discrete values that the
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

    setMapSplit(new ArrayList<>());
    int mapSplitPos = 0;
    int j = beginExampleIndex;
    for (int i = beginExampleIndex; i < endExampleIndex; i++) {
      if (!trainingSet.getExplanatoryValue(i, attribute.getIndex())
          .equals(trainingSet.getExplanatoryValue(i + 1, attribute.getIndex()))) {

        getMapSplit().add(new SplitInfo(trainingSet.getExplanatoryValue(i, attribute.getIndex()), j,
            i, mapSplitPos));
        j = i + 1;
        mapSplitPos++;
      }
    }
    getMapSplit()
        .add(new SplitInfo(trainingSet.getExplanatoryValue(endExampleIndex, attribute.getIndex()),
            j, endExampleIndex, mapSplitPos));

  }

  /**
   * Method implemented from the abstract super class SplitNode, compares the
   * value of the discrete attribute taken as input with each of the splitValue of
   * the SplitInfo inside the ArrayList mapSplit. If it is equal to one of them,
   * returns its index in the list, else returns -1.
   * 
   * @param value Value of the discrete attribute that we want to find.
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
    return "DISCRETE " + super.toString();
  }

}