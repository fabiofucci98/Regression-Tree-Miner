package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Uses a DbAccess to query the database.
 * 
 * @author Fabio
 *
 */
public class TableData {
  /**
   * Object of class DbAccess used to query the database.
   */
  private final DbAccess db;

  /**
   * Initializes the attribute db.
   * 
   * @param db DbAccess to assign.
   */
  public TableData(DbAccess db) {
    this.db = db;
  }

  /**
   * Queries the database to get a list of examples that represent out table.
   * 
   * @param table Name of the table in the database.
   * @return List of Example contained in our table.
   * @throws SQLException      If there are problems executing the query
   * @throws EmptySetException If the table is the empty.
   */
  public List<Example> getTransitions(String table) throws SQLException, EmptySetException {
    LinkedList<Example> transSet = new LinkedList<Example>();
    TableSchema ts = new TableSchema(db, table);
    String query = "SELECT ";
    for (int i = 0; i < ts.getNumberOfAttributes(); i++) {
      Column c = ts.getColumn(i);
      if (i > 0) {
        query += ",";
      }
      query += c.getColumnName();
    }

    if (ts.getNumberOfAttributes() == 0) {
      throw new SQLException();
    }
    query += (" FROM " + table);

    Statement statement = db.getConnection().createStatement();
    ResultSet rs = statement.executeQuery(query);
    boolean empty = true;
    while (rs.next()) {
      empty = false;
      Example currentTuple = new Example();
      for (int i = 0; i < ts.getNumberOfAttributes(); i++) {
        if (ts.getColumn(i).isNumber()) {
          currentTuple.add(rs.getDouble(i + 1));
        } else {
          currentTuple.add(rs.getString(i + 1));
        }
      }
      transSet.add(currentTuple);
    }
    statement.close();
    rs.close();
    if (empty) {
      throw new EmptySetException();
    }
    return transSet;

  }

  /**
   * Queries the database to get the set of all the distinct values contained in
   * our table in the Column column.
   * 
   * @param table  Name of the table in the database.
   * @param column Column to get our distinct values from.
   * @return Set of distinct values in the Column column of our table.
   * @throws SQLException If there are problems executing the query.
   */
  public Set<Object> getDistinctColumnValues(String table, Column column)
      throws SQLException, NullTupleException {
    Set<Object> set = new TreeSet<Object>();
    Statement s = db.getConnection().createStatement();

    ResultSet r = s.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table);

    boolean isNumber = column.isNumber();
    try {
      while (r.next()) {
        if (isNumber) {
          set.add(r.getFloat(column.getColumnName()));
        } else {
          set.add(r.getString(column.getColumnName()));
        }
      }
      r.close();
      return set;
    } catch (NullPointerException e) {
      throw new NullTupleException();
    }
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    return db.toString();
  }

}
