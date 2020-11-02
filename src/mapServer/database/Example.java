package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Used to store a row of the table.
 * 
 * @author Fabio
 *
 */
public class Example implements Comparable<Example>, Iterable<Object> {
  /**
   * List containing all the discrete or continuous values contained in a row of
   * the training set.
   */
  private final List<Object> example = new ArrayList<Object>();

  /**
   * Adds the object o to example.
   * 
   * @param o object to be added.
   */
  public void add(Object o) {
    example.add(o);
  }

  /**
   * Returns the object indexed by i in example.
   * 
   * @param i Index of the object.
   * @return Object indexed by i in example..
   */
  public Object get(int i) {
    return example.get(i);
  }

  /**
   * Compares this Example with ex.
   * 
   * @param ex Example to compare to this Object.
   */
  @SuppressWarnings("unchecked")
  public int compareTo(Example ex) {

    int i = 0;
    for (Object o : ex.example) {
      if (!o.equals(this.example.get(i))) {
        return ((Comparable<Object>) o).compareTo(example.get(i));
      }
      i++;
    }
    return 0;
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    for (Object o : example) {
      str.append(o.toString()).append(" ");
    }
    return str.toString();
  }

  /**
   * Returns an iterator to example.
   */
  @Override
  public Iterator<Object> iterator() {
    return example.iterator();
  }

}