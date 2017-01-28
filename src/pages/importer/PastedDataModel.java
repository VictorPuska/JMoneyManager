package pages.importer;

import application.Application;
import application.Database;
import zz.DbTable;
import zz.DbTableRow;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 * <code>PastedDataModel</code> represents the data pasted from the clipboard. Each pasted row is stored as a
 * a <code>PastedDataRow</code> which stores the original text, the delimited columns and other meta data.
 * Created by VJP on 20/08/2016.
 * @see PastedDataRow
 */
public class PastedDataModel extends AbstractTableModel{

    static private DateFormat DATEFORMAT_DDMMMYYYY = new SimpleDateFormat("dd-MMM-yyyy");
    static private DateFormat DATEFORMAT_DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");

    private ArrayList<PastedDataRow> rows = new ArrayList<>();
    private int columnCount = 0;
    private String columnTypes[];

    public PastedDataModel() {
        try {
            String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            String lines[] = data.split("\n");
            int maxCol = 0;
            // Determine the maximum column count.
            for (String line: lines) {
                PastedDataRow row = new PastedDataRow(line);
                rows.add(row);
                if(row.columnCount() > maxCol)
                    maxCol = row.columnCount();
            }
            columnCount = maxCol;
            columnTypes = new String[columnCount];
            for(int j = 0; j< maxCol; j++)
                columnTypes[j] = "Skip";
        }
        catch (Exception e1){
            Application.fatalError(e1);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PastedDataRow row = rows.get(rowIndex);
        return row.getColumn(columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        PastedDataRow row = rows.get(rowIndex);
        row.setColumn(columnIndex, (String) aValue);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //return super.isCellEditable(rowIndex, columnIndex);
        return true;
    }

    public boolean getErrorAt(int rowIndex, int columnIndex) {
        PastedDataRow row = rows.get(rowIndex);
        return row.isError(columnIndex);
    }

    public void setColumnType(int col, String typ) {
        String typ2 = typ.substring(0,4);
        columnTypes[col] = typ;
        for (PastedDataRow row: rows) {
            String c1 = row.getColumn(col);
            boolean valid = true;
            if(typ2.equals("Date"))
                valid = (isDate(c1, DATEFORMAT_DDMMMYYYY) || isDate(c1, DATEFORMAT_DDMMYYYY) );
            if (typ2.equals("Amou"))
                valid = isDecimal(c1);
            row.setError(col, !valid);
        }
    }

    private boolean isDate(String date, DateFormat format) {
        try {
            Date d = format.parse(date);
        }
        catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean isDecimal(String amt) {
        try {
            BigDecimal d = new BigDecimal(amt);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private Date toDate(String s) {
        try {
            if (isDate(s, DATEFORMAT_DDMMMYYYY))
                return DATEFORMAT_DDMMMYYYY.parse(s);
            if (isDate(s, DATEFORMAT_DDMMYYYY))
                return DATEFORMAT_DDMMYYYY.parse(s);
            throw(new Exception("Unexpected data format error in PastedDataModel"));
        }
        catch (Exception e){
            Application.fatalError(e);
        }
        return null;
    }

    private BigDecimal toAmount(String s) {
        try {
            BigDecimal bd = new BigDecimal(s);
            return bd;
        }
        catch (Exception e) {
            Application.fatalError(e);
        }
        return null;
    }

    public boolean hasErrors() {
        for(PastedDataRow r:this.rows)
            if(r.hasErrors())
                return true;
        return false;
    }

    public int saveData(int account) {

        int count = 0;

        try {
            Database.conn.setAutoCommit(false);
            DbTable imports = new DbTable(Database.conn, "IMPORTS");
            DbTable trans = new DbTable(Database.conn, "TRANSACTIONS");
            DbTableRow impRow = imports.newRow();
            impRow.setColumn("IMPORT_ACCOUNT", account);
            impRow.setColumn("IMPORT_DATETIME", Date.from(Instant.now()));
            impRow.setColumn("IMPORT_TYPE", "A");
            imports.insertRow(impRow);
            int importId = (int) impRow.getColumn("IMPORT_UID");

            for(PastedDataRow row:this.rows) {
                DbTableRow tran = trans.newRow();
                tran.setColumn("TRANSACTION_IMPORT", importId);
                tran.setColumn("TRANSACTION_SOURCE_DATA", row.getPastedText());
                tran.setColumn("TRANSACTION_DUPLICATE", false);
                tran.setColumn("TRANSACTION_CARDHOLDER", "");
                for(int j=0; j<columnTypes.length; j++) {
                    String ctype = columnTypes[j];
                    String cvalue = row.getColumn(j);
                    if(ctype.equals("Date"))
                        tran.setColumn("TRANSACTION_DATE", toDate(cvalue));
                    if(ctype.equals("Amount +"))
                        tran.setColumn("TRANSACTION_AMOUNT", toAmount(cvalue));
                    if(ctype.equals("Amount -"))
                        tran.setColumn("TRANSACTION_AMOUNT", toAmount(cvalue).negate());
                    if(ctype.equals("Memo"))
                        tran.setColumn("TRANSACTION_DESCRIPTION", cvalue);
                }
                trans.insertRow(tran);
                count += 1;
            }
            Database.conn.commit();
            Database.conn.setAutoCommit(true);
        }
        catch (SQLException e) {
            Application.fatalError(e);
        }
        return count;
    }

    /**
     * <code>PastedDataRow</code> encapsulates a single row of text pasted from the clipboard.  This class is
     * used by <code>PastedDataModel</code> to store individual rows copied from the clipboard.
     * @see PastedDataModel
     */
    private class PastedDataRow {

        private String pastedText;
        private String[] columns;
        private boolean[] err;

        public PastedDataRow(String text) {
            pastedText = text;
            columns = text.split("\t");
            err = new boolean[columns.length];
            for(boolean x: err)
                x = false;
        }

        public void setError(int col, boolean value) {
            err[col] = value;
        }

        public boolean isError(int col) {
            return err[col];
        }

        public boolean hasErrors() {
            for(boolean e:err)
                if(e)
                    return true;
            return false;
        }

        public void setColumn(int col, String value) {
            columns[col] = value;
        }

        public String getColumn(int col) {
            return columns[col];
        }

        public int columnCount() {
            return columns.length;
        }

        public String getPastedText() {
            return pastedText;
        }
    }
}
