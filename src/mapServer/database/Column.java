package database;

/**
 * Class used to model the abstraction of a column in a relational database. It
 * stores the name and data type of the column.
 * 
 * @author Fabio
 *
 */
public class Column {

  /**
   * Name of the column in the relational database.
   */
  private final String name;
  /**
   * Type of the column in the relational database (Numerical or not).
   */
  private final String type;

  /**
   * Initializes the attributes name and type.
   * 
   * @param name name of the column.
   * @param type data type of the column.
   */
  Column(String name, String type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Returns the name of the column.
   * 
   * @return Value of name.
   */
  public String getColumnName() {
    return name;
  }

  /**
   * Checks if the type of the column is numerical.
   * 
   * @return True if numerical, else False.
   */
  public boolean isNumber() {
    return type.equals("number");
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return name + ":" + type;
  }
}