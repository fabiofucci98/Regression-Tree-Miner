package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Class used to model the scheme of our table.
 * 
 * @author Fabio
 *
 */
public class TableSchema implements Iterable<Column> {
  /**
   * List of Column contained in the training set.
   */
  private final List<Column> tableSchema = new ArrayList<Column>();

  /**
   * Initializes a List of Column by querying the database.
   * 
   * @param db        DbAccess used to query the database.
   * @param tableName Name of the table in our database.
   * @throws SQLException If there are problems communicating with the database,
   */
  public TableSchema(DbAccess db, String tableName) throws SQLException {

    HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();

    mapSQL_JAVATypes.put("CHAR", "string");
    mapSQL_JAVATypes.put("VARCHAR", "string");
    mapSQL_JAVATypes.put("LONGVARCHAR", "string");
    mapSQL_JAVATypes.put("BIT", "string");
    mapSQL_JAVATypes.put("SHORT", "number");
    mapSQL_JAVATypes.put("INT", "number");
    mapSQL_JAVATypes.put("LONG", "number");
    mapSQL_JAVATypes.put("FLOAT", "number");
    mapSQL_JAVATypes.put("DOUBLE", "number");

    Connection con = db.getConnection();
    DatabaseMetaData meta = con.getMetaData();
    ResultSet res = meta.getColumns(null, null, tableName, null);

    while (res.next()) {

      if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
        tableSchema.add(new Column(res.getString("COLUMN_NAME"),
            mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
      }

    }
    res.close();

  }

  /**
   * Returns the number of columns contained in our table.
   * 
   * @return Size of attribute TableSchema.
   */
  public int getNumberOfAttributes() {
    return tableSchema.size();
  }

  /**
   * Returns the column indexed by index in tableSchema.
   * 
   * @param index Index of our column.
   * @return Column indexed by index.
   */
  public Column getColumn(int index) {
    return tableSchema.get(index);
  }

  /**
   * Retruns an iterator to tableSchema.
   */
  @Override
  public Iterator<Column> iterator() {
    return tableSchema.iterator();
  }

  /**
   * Returns a string containing all the class info.
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (Column column : tableSchema) {
      stringBuilder.append(column.toString() + "\n");
    }
    return stringBuilder.toString();
  }

}
