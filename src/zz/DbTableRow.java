package zz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * <p>A <code>DbTableRow</code> represents a single row within a <code>DbTable</code> and must be associated
 * with a <code>DbTable</code> record.</p>
 * <p>The <code>DbTableRow</code> will contain a field for each column in the <code>DbTable</code>.  The
 * <code>DbTableRow</code> tracks usage of fields and the <code>DbTable</code> takes this into account
 * when generating INSERT and UPDATE statements.</p>
 * @author VJP 25/11/2016
 * @version 1.0
 * @see DbTableRow
 * @see DbColumnInfo
 */
public class DbTableRow {

    private DbTable table;
    private boolean isNew = false;
    //private boolean isDeleted = false;
    private HashMap<String,Column> columns = new HashMap<>();

    /**
     * Creates a new empty DbTableRow.  All columns are initialised to an unchanged/unset state.
     * @param table <code>DbTable</code> - the table this row belongs to.
     */
    public DbTableRow(DbTable table) {
        this.table = table;
    }

    /**
     * Creates a new empty DbTableRow.  All columns are initialised to an unchanged/unset state.
     * The new row status is flagged according to the <code>isNew</code> parameter..
     * @param table <code>DbTable</code> - the table this row belongs to.
     * @param isNew <code>boolean</code> - flags whether this is treated as a new row not yet inserted into the
     *              database.
     */
    public DbTableRow(DbTable table, boolean isNew) {
        this(table);
        this.isNew = isNew;
    }

    /**
     * <p>Creates a new <code>DbTableRow</code>.  Matching columns in the <code>ResultSet</code> are copied to the
     * corresponding field in the <code>DbTableRow</code> and those fields are flagged as set and unchanged.
     * <p>The <code>ResultSet</code> cursor should be advanced to the row for which you wish to create the
     * <code>DbTableRow</code> object.</p>
     * @param table <code>DbTableRow</code> - the table this row belongs to.
     * @param rs <code>ResultSet</code> - source data used to initialise the object.
     * @throws Exception
     */
    public DbTableRow(DbTable table, ResultSet rs) throws SQLException {
        this(table);
        for(int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
            String name = rs.getMetaData().getColumnName(j + 1);
            if (table.isColumn(name))
                columns.put(name, new Column(rs.getObject(j+1),false));
        }
    }

    /**
     * Sets the column identified by <code>colName</code> to the Object <code>value</code>.  The column is
     * flagged as changed.
     * @param colName <code>String</code> - the name of the column
     * @param value <code>Object</code> - the value for the column.
     */
    public void setColumn(String colName, Object value) {
        int colno = table.columnNameToNumber(colName); // test valid column
        columns.put(colName, new Column(value, true));
    }

    /**
     * Sets the column identified by <code>colNumber</code> to the Object <code>value</code>.  The column is
     * flagged as changed.
     * @param colNumber <code>String</code> - the number of the column
     * @param value <code>Object</code> - the value for the column.
     */
    public void setColumn(int colNumber, Object value) {
        String colName = table.columnNumberToName(colNumber);
        columns.put(colName, new Column(value, true));
    }

    /**
     * Returns the current value of the column.  Returns <code>null</code> if the column has not been intilaised.
     * @param colName <code>String</code> - the name of the column
     * @return <code>Object</code> - the column's value, or <code>null</code> if not set.
     */
    public Object getColumn(String colName) {
        int colno = table.columnNameToNumber(colName); // test valid column
        if (columns.containsKey(colName))
            return columns.get(colName).value;
        else
            return null;
    }

    /**
     * Returns the current value of the column.  Returns <code>null</code> if the column has not been intilaised.
     * @param colNumber <code>int</code> - the number of the column
     * @return <code>Object</code> - the column's value, or <code>null</code> if not set.
     */
    public Object getColumn(int colNumber) {
        String colName = table.getColumnName(colNumber);
        if (columns.containsKey(colName))
            return columns.get(colName).value;
        else
            return null;
    }

    /**
     * Saves the row to the database.  The status of the <code>DbTableRow</code> record (new, deleted, changed) is
     * used to determined the type of update function (INSERT, UPDATE) to be used.
     * @throws SQLException
     */
    public void save() throws SQLException {
        if (this.isNew)
            this.table.insertRow(this);
        else
            if (this.isChanged())
                this.table.updateRow(this);
    }

    /**
     * Returns <code>true</code> if the column has a set value.  Returns <code>false</code> if the column value
     * has not been initialised.
     * @param colName <code>String</code> - the name of the column.
     * @return <code>boolean</code> - <code>true</code> of the column has been initialised.
     */
    public boolean isSet(String colName) {
        int colno = table.columnNameToNumber(colName); // test valid column
        return columns.containsKey(colName);
    }

    /**
     * Returns <code>true</code> if the column has a set value.  Returns <code>false</code> if the column value
     * has not been initialised.
     * @param colNumber <code>int</code> - the number of the column.
     * @return <code>boolean</code> - <code>true</code> of the column has been initialised.
     */
    public boolean isSet(int colNumber) {
        String colName = table.columnNumberToName(colNumber);
        return columns.containsKey(colName);
    }

    /**
     * Returns <code>true</code> if the column's value has changed.  Returns <code>false</code> if the column value
     * has not been changed.
     * @param colName <code>String</code> - the name of the column.
     * @return <code>boolean</code> - <code>true</code> of the column has been changed.
     */
    public boolean isChanged(String colName) {
        int colno = table.columnNameToNumber(colName); // test valid column
        if (columns.containsKey(colName))
            return columns.get(colName).isChanged;
        else
            return false;
    }

    /**
     * Returns <code>true</code> if the column's value has changed.  Returns <code>false</code> if the column value
     * has not been changed.
     * @param colNumber <code>int</code> - the number of the column.
     * @return <code>boolean</code> - <code>true</code> of the column has been changed.
     */
    public boolean isChanged(int colNumber) {
        String colName = table.columnNumberToName(colNumber);
        return columns.get(colName).isChanged;
    }

    /**
     * Returns <code>true</code> if any column in the row has been changed..  Returns <code>false</code> if the
     * row has been unchanged.
     * @return <code>boolean</code> - <code>true</code> of the row has been changed.
     */
    public boolean isChanged() {
        for(Column c: columns.values())
            if (c.isChanged)
                return true;
        return false;
    }

    /**
     * Returns <code>true</code> if this record is a newly created and not inserted into the database.  This
     * flag is used by the <code>save()</code> method to determine what type of SQL action is required
     * to save the record to the datanase (INSERT, DELETE, UPDATE).
     * @see #save()
     * @return <code>boolean</code> - <code>true</code> if the row has not been inserted into the database.
     */
    public boolean isNew() {
        return this.isNew;
    }

    public void setSaved() {
        this.isNew = false;
        for (Column c: this.columns.values())
            c.isChanged = false;
    }

    @Override
    public String toString() {
        String s = "<" + this.table.getTableName();
        String delim = ":";
        for(String pk:this.table.getPrimaryKeyNames()) {
            s += delim + pk + "=" + this.getColumn(pk);
            delim=",";
        }
        return s += ">";
    }

    private class Column {
        Object value = null;
        boolean isChanged = false;

        Column(Object value, boolean isChanged) {
            this.value = value;
            this.isChanged = isChanged;
        }

        void setValue(Object value) {
            this.value = value;
            this.isChanged = true;
        }
    }
}
