package data;

/**
 * Extends class Attribute to represent a continuous attribute.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class ContinuousAttribute extends Attribute {

  /**
   * Initializes the attributes name and index by calling the super constructor.
   * 
   * @param name  symbolic name of the attribute
   * @param index numeric identifier of the attribute
   */
  ContinuousAttribute(String name, int index) {
    super(name, index);
  }

}