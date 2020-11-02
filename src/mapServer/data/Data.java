package data;

import database.Column;
import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NullTupleException;
import database.TableData;
import database.TableSchema;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Models the set of the training examples.
 * 
 * 
 * @author Fabio
 *
 */
public class Data {
  /**
   * List of Example containing all the training data.
   */
  private final List<Example> data;
  /**
   * Number of Examples in the training set.
   */
  private final int numberOfExamples;
  /**
   * List of Attribute contained in our training set.
   */
  private final List<Attribute> explanatorySet;
  /**
   * ContinuousAttribute representing the class attribute of the training set.
   */
  private final ContinuousAttribute classAttribute;

  /**
   * Initializes an object of class Data. It connects to the database and
   * retrieves all the information needed to create the regression tree.
   * 
   * @param tableName Name of the SQL table we want to acquire our training set
   *                  from.
   * @throws TrainingDataException If it's not possible to acquire the training
   *                               set. This happens when the table does not
   *                               exist, when it's empty or with missing
   *                               informations or when it's impossible to connect
   *                               to the database.
   */
  @SuppressWarnings("unchecked")
  public Data(String tableName) throws TrainingDataException {
    explanatorySet = new ArrayList<>();
    try {
      TableSchema ts;
      DbAccess db = new DbAccess();

      try {
        db.initConnection();
      } catch (DatabaseConnectionException e) {
        throw new TrainingDataException("Could not connect to the database");
      }
      try {
        ts = new TableSchema(db, tableName);
      } catch (SQLException e) {
        throw new TrainingDataException(e);
      }
      if (ts.getNumberOfAttributes() == 0) {
        throw new TrainingDataException("The table does not exist");
      } else if (ts.getNumberOfAttributes() < 2) {
        throw new TrainingDataException("The table has less than two columns");
      }

      TableData td = new TableData(db);

      try {
        data = td.getTransitions(tableName);
      } catch (EmptySetException e) {
        throw new TrainingDataException("There are no examples in this table");
      }

      int i;
      Column column;
      Set<String> discreteValues;
      for (i = 0; i < ts.getNumberOfAttributes() - 1; i++) {
        column = ts.getColumn(i);

        if (column.isNumber()) {
          explanatorySet.add(new ContinuousAttribute(column.getColumnName(), i));
        } else {
          try {
            discreteValues = (TreeSet<String>) (TreeSet<?>) td.getDistinctColumnValues(tableName,
                column);
          } catch (NullTupleException e) {
            throw new TrainingDataException("The table contains a null tuple");

          }
          explanatorySet.add(new DiscreteAttribute(column.getColumnName(), i, discreteValues));
        }
      }
      column = ts.getColumn(i);
      if (!column.isNumber()) {
        throw new TrainingDataException(
            "The attribute corresponding to the last column is not numerical");
      }
      classAttribute = new ContinuousAttribute(column.getColumnName(), i);
      numberOfExamples = data.size();
      db.closeConnection();
    } catch (SQLException e) {
      throw new TrainingDataException(e);
    }
  }

  /**
   * Returns the cardinality of the examples set.
   * 
   * @return Values of number of examples.
   */
  public int getNumberOfExamples() {
    return numberOfExamples;
  }

  /**
   * Returns the cardinality of the explanatory set.
   * 
   * @return Size of explanatorySet.
   */
  public int getNumberOfExplanatoryAttributes() {
    return explanatorySet.size();
  }

  /**
   * Returns the class attribute for the example indexed by exampleIndex.
   * 
   * @param exampleIndex index of the example.
   * @return The value of the class attribute.
   */
  public Double getClassValue(int exampleIndex) {
    return (Double) data.get(exampleIndex).get(classAttribute.getIndex());
  }

  /**
   * Returns the value of the attribute indexed by attributeIndex for the example
   * exampleIndex.
   * 
   * @param exampleIndex   index of the example.
   * @param attributeIndex index of the attribute.
   * @return The value of the attribute indexed by attributeIndex for the example
   *         at index exampleIndex.
   */
  public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
    return data.get(exampleIndex).get(attributeIndex);
  }

  /**
   * Returns the attribute indexed by index in the explanatory set.
   * 
   * @param index index of the attribute.
   * @return The value of the attribute indexed by index.
   */
  public Attribute getExplanatoryAttribute(int index) {
    return explanatorySet.get(index);
  }

  /**
   * Returns the class attribute.
   * 
   * @return The value of class attribute.
   */
  public ContinuousAttribute getClassAttribute() {
    return classAttribute;
  }

  /**
   * Orders the subset of examples within the range
   * [beginExampleIndex,endExampleIndex], compared to the Attribute attribute. It
   * uses the quicksort algorithm to sort an array of integers using "&lt;=" as
   * the total order relation. The array is given from the values that the input
   * attribute assumes. Uses the following sub-methods, private void
   * quicksort(Attribute attribute, int inf, int sup), private int
   * partition(DiscreteAttribute attribute, int inf, int sup), private int
   * partition(ContinuousAttribute attribute, int inf, int sup), private void
   * swap(int i,int j).
   * 
   * 
   * @param attribute         Attribute used to sort the array
   * @param beginExampleIndex lower bound of the partition.
   * @param endExampleIndex   upper bound of the partition.
   */
  public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {

    quicksort(attribute, beginExampleIndex, endExampleIndex);
  }

  /**
   * Swaps the examples i and j in data.
   * 
   * @param i index of the example to swap
   * @param j index of the example to swap
   */
  private void swap(int i, int j) {
    Collections.swap(data, i, j);
  }

  /**
   * Partitions the array compared to the explanatory value of the
   * DiscreteAttribute attribute for the example at index (inf+sup)/2 and returns
   * the separation point.
   * 
   * @param attribute DiscreteAttribute used to partition the array
   * @param inf       lower bound of the partition.
   * @param sup       upper bound of the partition.
   * @return The separation point of the array.
   */
  private int partition(DiscreteAttribute attribute, int inf, int sup) {
    int i;
    int j;

    i = inf;
    j = sup;
    int med = (inf + sup) / 2;
    int temp = attribute.getIndex();
    String x = (String) getExplanatoryValue(med, temp);
    swap(inf, med);

    while (true) {

      while (i <= sup
          && ((String) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
        i++;

      }

      while (((String) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
        j--;

      }

      if (i < j) {
        swap(i, j);
      } else {
        break;
      }
    }
    swap(inf, j);
    return j;

  }

  /**
   * Partitions the array compared to the explanatory value of the
   * ContinuousAttribute attribute for the example at index (inf+sup)/2 and
   * returns the separation point.
   * 
   * @param attribute ContinuousAttribute used to partition the array
   * @param inf       lower bound of the partition.
   * @param sup       upper bound of the partition.
   * @return The separation point of the array.
   */
  private int partition(ContinuousAttribute attribute, int inf, int sup) {
    int i;
    int j;

    i = inf;
    j = sup;
    int med = (inf + sup) / 2;
    int temp = attribute.getIndex();
    Object tempO = getExplanatoryValue(med, temp);
    Double x = (Double) tempO;
    swap(inf, med);

    while (true) {

      while (i <= sup
          && ((Double) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
        i++;

      }

      while (((Double) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
        j--;

      }

      if (i < j) {
        swap(i, j);
      } else {
        break;
      }
    }
    swap(inf, j);
    return j;

  }

  /**
   * Sorts in ascending order compared to the Attribute attribute the list Data.
   * 
   * @param attribute Attribute used to sort the array
   * @param inf       lower bound of the training set.
   * @param sup       upper bound of the training set.
   */
  private void quicksort(Attribute attribute, int inf, int sup) {

    if (sup >= inf) {

      int pos;
      if (attribute instanceof DiscreteAttribute) {
        pos = partition((DiscreteAttribute) attribute, inf, sup);
      } else {
        pos = partition((ContinuousAttribute) attribute, inf, sup);
      }

      if ((pos - inf) < (sup - pos + 1)) {
        quicksort(attribute, inf, pos - 1);
        quicksort(attribute, pos + 1, sup);
      } else {
        quicksort(attribute, pos + 1, sup);
        quicksort(attribute, inf, pos - 1);
      }

    }

  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    StringBuilder value = new StringBuilder();
    for (int i = 0; i < numberOfExamples; i++) {
      for (int j = 0; j < explanatorySet.size(); j++) {
        value.append(data.get(i).get(j) + ",");
      }

      value.append(data.get(i).get(explanatorySet.size()) + "\n");
    }
    return value.toString();
  }
}