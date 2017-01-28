package zz;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import static java.sql.Types.*;

/**
 * Created by VJP on 18/12/2016.
 */
public class DbTableRowSet extends AbstractTableModel {

    private DbTable table;
    private ArrayList<DbTableRow> rows = new ArrayList<>();
    private boolean viewMode = false;

    public DbTableRowSet(DbTable table) {
        this.table = table;
    }

    public void fill() {
        try {
            this.rows = table.selectAll();
        } catch (SQLException e) {
            dbError(e);
        }
    }

    public void fill(ArrayList<DbTableRow> rows) {
        this.rows = rows;
    }

    public DbTableRow newRow() {
        return table.newRow();
    }

    public void addRow(DbTableRow row) {
        try {
            table.insertRow(row);
            rows.add(row);
            fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
            //fireTableRowsInserted(2,2);
        } catch (SQLException e) {
            dbError(e);
        }
    }

    public DbTableRow getRow(int row) {
        return rows.get(row);
    }

    public void deleteRow(int row) {
        try {
            DbTableRow r = rows.get(row);
            this.table.deleteRow(r);
            rows.remove(row);
            fireTableRowsDeleted(row, row);
        } catch (SQLException e) {
            dbError(e);
        }
    }

    public boolean isViewMode() {
        return viewMode;
    }

    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    @Override
    public String getColumnName(int column) {
        return table.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        DbColumnInfo info = table.getColumnInfo(columnIndex);
        switch (info.getSqlType()) {
            case BIT:
            case TINYINT:
            case SMALLINT:
            case INTEGER:
                return Integer.class;
            case BIGINT:
                return BigInteger.class;
            case FLOAT:
            case REAL:
                return Float.class;
            case DOUBLE:
            case NUMERIC:
                return Double.class;
            case DECIMAL:
                return BigDecimal.class;
            case CHAR:
            case VARCHAR:
            case LONGNVARCHAR:
                return String.class;
            case DATE:
                 return Date.class;
            case TIME:
                return Time.class;
            case TIMESTAMP:
                return Timestamp.class;
            case BOOLEAN:
                return Boolean.class;
            default:
                return Object.class;
        }
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return table.columnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getColumn(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
        try {
            rows.get(rowIndex).setColumn(columnIndex, aValue);
            table.updateRow(rows.get(rowIndex));
        } catch (SQLException e) {
            dbError(e);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(isViewMode())
            return false;
        DbColumnInfo info = table.getColumnInfo(columnIndex);
        if(info.isAutoIncrement())
            return false;
        return info.isBoundColumn();
    }

    public static void dbError(Exception e) {
        JOptionPane.showMessageDialog(
                new JFrame(),
                e,
                "DbTableRowSet Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        System.exit(-1);
    }

}
