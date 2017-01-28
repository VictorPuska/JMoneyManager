package zz;

import application.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 * A representation of a SQL Table. A <code>DbTable</code> exposes a SQL table to a java application with basic
 * CRUD capabilities.  Uses a <code>DbTableRow</code> to represent table rows.
 * <p><b>Constructors</b><br>
 * <ul>
 *     <li><code>DbTable(conn, tableName)</code></li>
 *     <li><code>DbTable(conn, tableName, columns...)</code> - creates a DbTable retrieving the nominated columns only</li>
 * </ul>
 * <p><b>Methods</b></p>
 * <ul>
 *     <li><code>getTableName()</code> - returns the table's name</li>
 *     <li><code>newRow()</code> - returns an empty row (<code>DbTableRow</code>)</li>
 *     <li><code>columnCount()</code> - the number of columns in the <code>DbTable</code></li>
 *     <li><code>isColumn(string)</code> - <code>true</code> if the column name exists</li>
 *     <li><code>isPrimaryKey(string)</code> - <code>true</code> if the column is part of the primary key</li>
 *     <li><code>getPrimaryKeyNames()</code> - returns a <code>String[]</code> of the primary key columns</li>
 *     <li><code>getColumnName(integer)</code> - gets the name for the specified column number</li>
 *     <li><code>columnNameToNumber(string)</code> - converts column name to number</li>
 *     <li><code>columnNumberToName(integer)</code> - converts column number to name
 *          (same as <code>getColumnName()</code>)</li>
 *     <li><code>selectAll()</code> - returns an <code>ArrayList</code> of <code>DbRows</code>
 *          representing the rows of this table</li>
 *     <li><code>orderBy(columns...)</code> - sets the sort order for <code>selectAll()</code></li>
 *     <li><code>orderBy()</code> - clears the sort order for <code>selectAll()</code></li>
 *     <li><code>selectSingle(key...)</code> - returns a single <code>DbTableRow</code> for the
 *          specified primary key</li>
 *     <li><code>updateRow(DbTableRow)</code> - update changed values of the row to the table</li>
 *     <li><code>insertRow(DbTableRow</code> - insert the <code>DbTableRow</code> into the talbe</li>
 *     <li><code>deleteRow(key...)</code> - deletes the row from the table for the nominated primary key</li>
 *     <li><code>deleteRow(DbTableRow)</code> - deletes the row from the table</li>
 *     <li><code>primaryKeyFilter()</code> - returns a <code>String</code> representing a parameterised
 *          <code>WHERE</code> clause with the primary key columns</li>
 * </ul>
 * @see DbTableRow
 * @author VJP 25/11/2016.
 * @version 1.0
 */
public class DbTable {

    private String tableName = null;
    private Connection conn = null;
    private ArrayList<DbColumnInfo> columnInfo = new ArrayList<>();
    //private HashMap<Integer,String> primaryKey = new HashMap<>();
    private String[] primaryKey;

    private String insertStmtSQL = "";
    private CallableStatement insertStmt;

    private PreparedStatement selectAllStmt = null;
    private PreparedStatement selectSingleStmt = null;
    private PreparedStatement deleteRowStmt = null;

    private String updateStmtSQL = "";
    private PreparedStatement updateStmt;

    private String orderBy = "";

    /**
     * Create a new <code>DbTable</code> object for the specified SQL table.
     * @param conn <code>Connection</code> - a database connection.
     * @param tableName <code>String</code> - the SQL table represented by this DbTable
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
     */
    public DbTable(Connection conn, String tableName) throws SQLException {
        this.tableName = tableName;
        this.conn = conn;
        try {
            // Get the column details
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, tableName, null);
            while (rs.next())
                columnInfo.add(new DbColumnInfo(rs));
            rs.close();
            // Retrieve the primary keys for the table.  Note, we do not assume that the keys are
            // returned in order.
            rs = meta.getPrimaryKeys(null, null, tableName);
            HashMap<Integer,String> map = new HashMap<>();
            while(rs.next())
                map.put(rs.getInt("KEY_SEQ"), rs.getString("COLUMN_NAME"));
            rs.close();
            primaryKey = new String[map.size()];
            for(int j=0; j < map.size(); j++)
                primaryKey[j] = map.get(j+1);
        }
        catch (Exception e) {
            throw(new SQLException("Error initialising table metadata:" + tableName, e));
        }
    }

    /**
     * Create a new <code>DbTable</code> object for the specified SQL table.  Only the columns listed
     * are accessed by database select and manipulation commands.
     * @param conn <code>Connection</code> - a database connection.
     * @param tableName <code>String</code> - the SQL table represented by this DbTable
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
     */
    public DbTable(Connection conn, String tableName, String... columns) throws SQLException {
        this(conn, tableName);
        columnInfo.removeIf( cinfo -> {
            // do not remove if primary key column
            if(isPrimaryKeyColumn(cinfo.getName()))
                return false;
            // do not remove if one of the nominated columns
            for(String c:columns)
                if (c.compareToIgnoreCase(cinfo.getName())==0)
                    return false;
            return true;
        });
    }

    /**
     * Add an unbound column to the table.
     * @param name <code>String</code> - The name of the column
     * @return <code>int</code> - the column number
     */
    public int addColumn(String name, int type) {
        DbColumnInfo cinfo = new DbColumnInfo(name, type);
        columnInfo.add(cinfo);
        return columnCount() - 1;
    }

    /**
     * Return the underlying table name.
     * @return <code>String</code> - The table name.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Returns a new, empty <code>DbRow</code> tied to the current table.
     * @return <code>DbRow</code> - empty <code>DbRow</code> record link to this table.
     */
    public DbTableRow newRow() {
        return new DbTableRow(this);
    }

    /**
     * Returns the number of columns in the table.
     * @return <code>int</code> - the number of columns in the table.
     */
    public int columnCount() {
        return columnInfo.size();
    }

    /**
     * Returns the column name for the column number.
     * @param colNumber <code>int</code> - the column number.
     * @return <code>String</code> - the column's name
     */
    public String getColumnName(int colNumber) {
        return columnInfo.get(colNumber).getName();
    }

    /**
     * Returns the column number for a given column name.  The column name is not case sensitive.
     * @param name <code>String</code> - the name of the column.
     * @return <code>int</code> - the column number.
     */
    public int columnNameToNumber(String name) {
        for(int j = 0; j < columnCount(); j++)
            if(getColumnName(j).compareToIgnoreCase(name)==0)
                return j;
        String msg = String.format("Column '%s' not found in table '%s'", name, this.tableName);
        throw new IndexOutOfBoundsException(msg);
    }

    /**
     * Returns the column name for the given column number.
     * @param columnNumber <code>int</code> - the column number.
     * @return <code>String</code> - the column name.
     */
    public String columnNumberToName(int columnNumber) {
        return getColumnName(columnNumber);
    }

    /**
     * Return <code>true</code> if the column exists in the <code>DbTable</code>.  Fields excluded
     * by the constructor will return false, or fields that do not exist in the underlying SQL table.
     * @param colName <code>String</code> - column name.
     * @return <code>boolean</code> - true if column exists.
     */
    public boolean isColumn(String colName) {
        for (DbColumnInfo cinfo : columnInfo)
            if (colName.equalsIgnoreCase(cinfo.getName()))
                return true;
        return false;
    }

    public DbColumnInfo getColumnInfo(int colNo) {
        return columnInfo.get(colNo);
    }

    public DbColumnInfo getColumnInfo(String colName) {
        return columnInfo.get(columnNameToNumber(colName));
    }

    /**
     * Returns a <code>String[]</code> array of the primary key column names.
     * @return <code>String[]</code> - primary key column names
     */
    public String[] getPrimaryKeyNames() {
        String key[] = new String[primaryKey.length];
        for(int j = 0; j< key.length; j++) {
            key[j] = primaryKey[j];
        }
        return key;
    }

    /**
     * Return <code>true</code> if the column exists in the primary key.
     * @param colName <code>String</code> - column name.
     * @return <code>boolean</code> - true if column exists.
     */
    public boolean isPrimaryKeyColumn(String colName) {
        for(String pk:primaryKey)
            if(colName.equalsIgnoreCase(pk))
                return true;
        return false;
    }

    /**
     * Returns an <code>ArrayList</code> of <code>DbRows</code> containing all the records of
     * the database table.
     * @return  <code>ArrayList<DbRow></code> - Array of <code>DbRow</code> records.
     */
    public ArrayList<DbTableRow> selectAll() throws SQLException {
        if (this.selectAllStmt == null) {
            String stmtSQL = "SELECT * FROM " + this.tableName + " " + this.orderBy;
            this.selectAllStmt = this.conn.prepareStatement(stmtSQL, ResultSet.TYPE_FORWARD_ONLY);
        }
        ArrayList<DbTableRow> list = new ArrayList<>();
        ResultSet rs = selectAllStmt.executeQuery();
        while (rs.next())
            list.add(new DbTableRow(this, rs));
        rs.close();
        return list;
    }

    public ArrayList<DbTableRow> selectAll(String tableView) throws SQLException {
        String stmtSQL = "SELECT * FROM " + tableView + " " + this.orderBy;
        PreparedStatement stmt = this.conn.prepareStatement(stmtSQL, ResultSet.TYPE_FORWARD_ONLY);
        ArrayList<DbTableRow> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
                list.add(new DbTableRow(this, rs));
        rs.close();
        return list;
    }

    public ArrayList<DbTableRow> selectWhere(String where, Object... params) throws SQLException {
        String stmtSQL = "SELECT * FROM " + this.tableName + " WHERE " + where + " " + this.orderBy;
        PreparedStatement stmt = this.conn.prepareStatement(stmtSQL, ResultSet.TYPE_FORWARD_ONLY);
        for(int j=0; j<params.length; j++)
            stmt.setObject(j+1, params[j]);
        ArrayList<DbTableRow> list = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
            list.add(new DbTableRow(this, rs));
        rs.close();
        return list;
    }

    /**
     * Set the sequence records are returned by <code>selectAll</code>.
     * @param col <code>String...</code> - list of column names
     */
    public void orderBy(String... col) {
        StringJoiner joiner = new StringJoiner(",");
        for(String s:col)
            if(isColumn(s))
                joiner.add(s);
            else {
                String msg = String.format("Column '%s' not found in table '%s'", s, this.tableName);
                throw new IndexOutOfBoundsException(msg);
            }
        this.orderBy = "ORDER BY " + joiner.toString();
    }

    /**
     * Removes the set record sequence.
     */
    public void orderBy() {
        this.orderBy = "";
    }

    /**
     * Selects a single row from the table using the primary key.
     * @param key <code>Object...</code> - Primary key values
     * @return <code>DbRow</code> - Single object or <code>null</code> if not found.
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
     */
    public DbTableRow selectSingle(Object... key) throws SQLException {
        if (this.selectSingleStmt==null) {
            String stmtSQL = String.format("SELECT * FROM %s WHERE %s", this.tableName, this.primaryKeyFilter());
            this.selectSingleStmt = Database.conn.prepareStatement(stmtSQL, ResultSet.TYPE_FORWARD_ONLY);
            System.out.println(stmtSQL);
        }
        for(int j=0; j < this.primaryKey.length; j++)
            this.selectSingleStmt.setObject(j+1, key[j]);
        ResultSet rs = selectSingleStmt.executeQuery();
        return ( rs.next() ? new DbTableRow(this, rs) : null);
    }

    /**
     * Updates a single row in the table.  Primary key and auto increment fields are not updated.
     * <code>updateRow</code> builds a <code>WHERE</code> clause based on the primary key of the table.  The
     * <code>SET</code> clause is built for all fields (excluding primary key and auto increment fields)
     * regardless of whether they have changed.
     * @param row <code>DbRow</code> - the row to update.
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
     */
    public void updateRow(DbTableRow row) throws SQLException {
        // Build the SET clause
        StringJoiner setClause = new StringJoiner(",");
        for(int j=0; j < this.columnCount(); j++) {
            String name = getColumnName(j);
            DbColumnInfo cinfo = columnInfo.get(j);
            if(row.isChanged(name) && cinfo.isBoundColumn() && !cinfo.isAutoIncrement() && !this.isPrimaryKeyColumn(name))
                 setClause.add(name + "=?");
        }
        String stmtSQL = String.format("UPDATE %s SET %s WHERE %s", this.tableName, setClause.toString(), this.primaryKeyFilter());
        System.out.println(stmtSQL);
        if(stmtSQL.compareTo(this.updateStmtSQL) != 0) {
            this.updateStmt = Database.conn.prepareStatement(stmtSQL);
            this.updateStmtSQL = stmtSQL;
        }
        int k = 1;
        for(int j=0; j < this.columnCount(); j++) {
            String name = getColumnName(j);
            DbColumnInfo info = getColumnInfo(j);
            if(row.isChanged(name) && !info.isAutoIncrement() && !this.isPrimaryKeyColumn(name) && info.isBoundColumn())
                this.updateStmt.setObject(k++, row.getColumn(name));
        }
        for(int j=0; j < this.primaryKey.length; j++)
            this.updateStmt.setObject(k++, row.getColumn(this.primaryKey[j]));
        this.updateStmt.executeUpdate();
    }

    /**
     * Inserts the row into the database.  An <code>INSERT</code> statement is constructed and prepared
     * for the columns referenced by the <code>DbRow</code>.  Auto increment fields are not included in
     * the insert statement.
     * @param row <code>DbRow</code> - the row to insert into the database.
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
     */
    public void insertRow(DbTableRow row) throws SQLException {
        // Construct the list of field names and values to be inserted.  Ignore auto increment fields.
        StringJoiner flds = new StringJoiner(",");
        StringJoiner vals = new StringJoiner(",");
        for(int j=0; j < this.columnCount(); j++) {
            String name = getColumnName(j);
            DbColumnInfo cinfo = columnInfo.get(j);
            if(row.isSet(name) && cinfo.isBoundColumn() && !cinfo.isAutoIncrement()) {
                flds.add(name);
                vals.add("?");
            }
        }
        // Generate & prepare the SQL statement. Reuse the previous prepared INSERT statement if identical.
        String stmt = String.format("INSERT INTO %s (%s) VALUES (%s)", this.tableName, flds.toString(), vals.toString());
        if (stmt.compareTo(insertStmtSQL) != 0) {
            this.insertStmtSQL = stmt;
            this.insertStmt = Database.conn.prepareCall(stmt,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY,
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);
        }
        // Set the parameters for the insert.  Ignore auto increment columns.
        for(int j=0, k=1; j < this.columnCount(); j++) {
            DbColumnInfo cinfo = columnInfo.get(j);
            if(row.isSet(j) && cinfo.isBoundColumn() && !cinfo.isAutoIncrement()) {
                insertStmt.setObject(k++, row.getColumn(j));
            }
        }
        // Execute the insert.
        insertStmt.executeUpdate();
        // Update any auto increment value
        ResultSet keyrs = insertStmt.getGeneratedKeys();
        if (keyrs.next()) {
            int key = keyrs.getInt(1);
            for (DbColumnInfo cinfo : this.columnInfo) {
                if (cinfo.isAutoIncrement()) {
                    row.setColumn(cinfo.getName(), keyrs.getObject(1));
                    break;
                }
            }
        }
        keyrs.close();
    }

    /**
     * Deletes the specified row from the table using the key specified.  Eg.
     * <pre>    table.deleteRow("SMITH", "JOHN")</pre>
     * @param key  <code>Object...</code> - primary key values.
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
     */
    public void deleteRow(Object... key) throws SQLException {
        if(this.deleteRowStmt==null) {
            String sql = String.format("DELETE FROM %s WHERE %s", this.tableName, this.primaryKeyFilter());
            this.deleteRowStmt = Database.conn.prepareStatement(sql);
        }
        for(int j=0; j<primaryKey.length; j++)
            this.deleteRowStmt.setObject(j+1, key[j]);
        this.deleteRowStmt.executeUpdate();
    }

    /**
     * Deletes the row corresponding to the <code>DbRow</code> object.
     * @param row <code>DbRow</code> - row object.
     * @throws SQLException - raises an exception if the underlying JDBC actions fail
    */
    public void deleteRow(DbTableRow row) throws SQLException {
        Object [] key = new Object[this.primaryKey.length];
        for(int j=0; j<this.primaryKey.length; j++)
            key[j] = row.getColumn(this.primaryKey[j]);
        deleteRow(key);
    }

    /**
     * Builds a paramaterised selection filter based on the primary key of the table.  Eg.:
     * <pre>
     *     FIELDA=? AND FIELDB=? AND FIELDC=?
     * </pre>
     * @return <code>String</code> - a parameterised selection expression.
     */
    public String primaryKeyFilter() {
        StringJoiner joiner = new StringJoiner(" AND ");
        for(int j = 0; j < this.primaryKey.length; j++)
            joiner.add(this.primaryKey[j] + "=?");
        return joiner.toString();
    }
}
