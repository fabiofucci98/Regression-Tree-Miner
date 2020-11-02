package tree;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeSet;
import server.UnknownValueException;

/**
 * Class used to model the abstraction "Regression Tree".
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class RegressionTree implements Serializable {
  /**
   * Root of the tree.
   */
  private Node root;
  /**
   * Child trees of this tree.
   */
  private RegressionTree[] childTree;

  /**
   * Instantiates a RegressionTree.
   */
  private RegressionTree() {
  }

  /**
   * Instantiates a RegressionTree and starts the learning phase on the input
   * training set.
   * 
   * @param trainingSet Collection of training examples.
   */
  public RegressionTree(Data trainingSet) {
    learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1,
        trainingSet.getNumberOfExamples() * 10 / 100);
  }

  /**
   * Checks if the sub set [begin,end] can be covered by a leaf node.
   * 
   * @param trainingSet             Collection of training examples.
   * @param begin                   Index of the first element of the sub set.
   * @param end                     Index of the last element of the sub set.
   * @param numberOfExamplesPerLeaf Minimum number of examples that a leaf should
   *                                have.
   * @return True if the sub set can be covered by a leaf node, else False.
   */
  private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
    return end - begin + 1 <= numberOfExamplesPerLeaf;
  }

  /**
   * Instantiates a new SplitNode (Continuous or Discrete) for each independent
   * attribute in the explanatory set, uses the one with the lower variance to
   * sort the training set in the range [begin,end], and returns the SplitNode
   * associated.
   * 
   * @param trainingSet Collection of training examples.
   * @param begin       Index of the first element of the sub set.
   * @param end         Index of the last element of the sub set.
   * @return The SplitNode with the lowest variance.
   * @throws NoBestExplanatoryValueFoundException If all the explanatory values
   *                                              for every example in the current
   *                                              sub set are equal.
   */
  private SplitNode determineBestSplitNode(Data trainingSet, int begin, int end)
      throws NoBestExplanatoryValueFoundException {

    TreeSet<SplitNode> ts = new TreeSet<SplitNode>();
    SplitNode min;
    SplitNode currentNode;

    for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
      Attribute attribute = trainingSet.getExplanatoryAttribute(i);
      if (attribute instanceof DiscreteAttribute) {
        currentNode = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) attribute);
      } else {
        currentNode = new ContinuousNode(trainingSet, begin, end, (ContinuousAttribute) attribute);

      }

      ts.add(currentNode);

    }
    min = ts.first();
    trainingSet.sort(min.getAttribute(), begin, end);
    return min;

  }

  /**
   * Recursive method used to generate the regression tree. If the current set of
   * examples can be covered by a leaf node then the root of the tree gets
   * assigned a new LeafNode in that range, else the output of the method
   * determineBestSplitNode gets assigned to the root and learnTree is recursively
   * called for every child of root.
   * 
   * @param trainingSet             Collection of training examples.
   * @param begin                   Index of the first element of the sub set.
   * @param end                     Index of the last element of the sub set.
   * @param numberOfExamplesPerLeaf Minimum number of examples that a leaf should
   *                                have.
   */
  private void learnTree(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
    if (isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)) {
      root = new LeafNode(trainingSet, begin, end);
    } else {
      try {
        root = determineBestSplitNode(trainingSet, begin, end);
        if (root.getNumberOfChildren() > 1) {
          childTree = new RegressionTree[root.getNumberOfChildren()];
          for (int i = 0; i < root.getNumberOfChildren(); i++) {
            childTree[i] = new RegressionTree();
            childTree[i].learnTree(trainingSet, ((SplitNode) root).getSplitInfo(i).getBeginIndex(),
                ((SplitNode) root).getSplitInfo(i).getEndIndex(), numberOfExamplesPerLeaf);
          }
        } else {
          root = new LeafNode(trainingSet, begin, end);
        }
      } catch (NoBestExplanatoryValueFoundException e) {
        root = new LeafNode(trainingSet, begin, end);
      }
    }
  }

  /**
   * Prints the tree to the screen.
   */
  public void printTree() {
    System.out.println("********* TREE **********\n");
    System.out.println(toString());
    System.out.println("*************************\n");
  }

  /**
   * Prints the tree rules to the screen.
   */
  public void printRules() {
    System.out.println("********* RULES *********\n");
    printRules("");
    System.out.println("\n*************************\n");
  }

  /**
   * Support method for printRules(). Recursively scans the tree concatenating the
   * current node info if it's a split node, else if it's a leaf node prints them
   * to the screen.
   * 
   * @param current String containing the info to print.
   */
  private void printRules(String current) {
    if (root instanceof LeafNode) {
      System.out.println(current + " ==> Class=" + ((LeafNode) root).getPredictedClassValue());
    } else {
      current += ((SplitNode) root).getAttribute();
      for (int i = 0; i < childTree.length; i++) {
        String temp = new String(current);
        SplitNode tempNode = (SplitNode) root;
        temp += tempNode.getSplitInfo(i).getComparator() + tempNode.getSplitInfo(i).getSplitValue();
        if (childTree[i].root instanceof LeafNode) {
          childTree[i].printRules(temp);
        } else {
          childTree[i].printRules(temp + " AND ");
        }
      }
    }
  }

  /**
   * Allows the user to make a prediction based on the current tree.
   * 
   * @param in  Input stream to communicate with the client.
   * @param out Output stream to communicate with the client.
   * @return The predicted class value.
   * @throws UnknownValueException  If the user input is not a integer between 0
   *                                and root.getNumberOfChildren() -1.
   * @throws IOException            If there are communication problems.
   * @throws ClassNotFoundException If the user input is not an instance of a
   *                                known class.
   */
  public Double predictClass(ObjectInputStream in, ObjectOutputStream out)
      throws UnknownValueException, IOException, ClassNotFoundException {
    if (root instanceof LeafNode) {
      out.writeObject("OK");
      return ((LeafNode) root).getPredictedClassValue();
    } else {
      int risp = -1;
      out.writeObject("QUERY");
      out.writeObject(((SplitNode) root).formulateQuery());
      risp = (int) in.readObject();
      if (risp < 0 || risp >= root.getNumberOfChildren()) {
        throw new UnknownValueException("The answer should be an integer between 0 and "
            + (root.getNumberOfChildren() - 1) + "!");
      } else {
        return childTree[risp].predictClass(in, out);
      }
    }
  }

  /**
   * Saves the tree to a file.
   * 
   * @param fileName Name of the file where to save the tree.
   * @throws ImpossibleSerializationException If there are serialization problems.
   */
  public void save(String fileName) throws ImpossibleSerializationException {
    try {
      FileOutputStream outFile = new FileOutputStream(fileName);
      ObjectOutputStream outStream = new ObjectOutputStream(outFile);
      outStream.writeObject(this);
      outStream.close();
    } catch (IOException e) {
      throw new ImpossibleSerializationException(e);
    }
  }

  /**
   * Loads the tree from a file.
   * 
   * @param fileName Name of the file where the tree is stored
   * @return Object of class RegressionTree loaded from file.
   * @throws ImpossibleSerializationException If there are serialization problems.
   */
  public static RegressionTree load(String fileName) throws ImpossibleSerializationException {
    try {
      FileInputStream inFile = new FileInputStream(fileName);
      ObjectInputStream inStream = new ObjectInputStream(inFile);
      RegressionTree loaded = (RegressionTree) inStream.readObject();
      inStream.close();
      return loaded;
    } catch (IOException | ClassNotFoundException e) {
      throw new ImpossibleSerializationException(e);
    }

  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    String tree = root.toString() + "\n";

    if (root instanceof SplitNode) {
      for (int i = 0; i < childTree.length; i++) {
        tree += childTree[i];
      }
    }
    return tree;
  }

}
