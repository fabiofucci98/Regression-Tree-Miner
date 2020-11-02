package data;

import java.util.Iterator;
import java.util.Set;

/**
 * Extends class Attribute to represent a Discrete Attribute.
 * 
 * @author Fabio
 *
 */
@SuppressWarnings("serial")
public class DiscreteAttribute extends Attribute implements Iterable<String> {
  /**
   * Set of all the discrete values that the attribute might assume.
   */
  private final Set<String> values;

  /**
   * Initializes the attribute values with the input set, then initializes the
   * attributes {@link name} and {@link index} by calling the super constructor.
   * 
   * @param name   symbolic name of the attribute
   * @param index  numeric identifier of the attribute
   * @param values set of all the possible strings that the attribute can assume
   */
  DiscreteAttribute(String name, int index, Set<String> values) {
    super(name, index);
    this.values = values;
  }

  /**
   * Returns the size of the set of values that the attribute might assume.
   * 
   * @return size of the attribute values
   */
  public int getNumberOfDistinctValues() {
    return values.size();
  }

  @Override
  public Iterator<String> iterator() {
    return values.iterator();
  }

}