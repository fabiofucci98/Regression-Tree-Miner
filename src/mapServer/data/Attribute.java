package data;

import java.io.Serializable;

/**
 * Abstract class used to model a discrete or continuous attribute.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public abstract class Attribute implements Serializable {
  /**
   * Attribute name in the explanatory set.
   */
  private final String name;
  /**
   * Attribute index in the explanatory set.
   */
  private final int index;

  /**
   * Initializes the attributes name and index.
   * 
   * @param name  symbolic name of the attribute
   * @param index numeric identifier of the attribute
   */
  Attribute(String name, int index) {
    this.name = name;
    this.index = index;
  }

  /**
   * Returns the symbolic name of the attribute.
   * 
   * @return value of name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the numeric identifier of the attribute.
   * 
   * @return value of index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return name;
  }

}