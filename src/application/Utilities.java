package application;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * Created by VJP on 28/01/2017.
 */
public class Utilities {

    public static TableColumn createTableColumn(int modelIndex, Object heading, int width) {
        TableColumn tc = new TableColumn(modelIndex, width);
        tc.setHeaderValue(heading);
        return tc;
    }

    public static TableColumn createTableColumn(AbstractTableModel model, String colName, Object heading, int width) {
        return createTableColumn(model.findColumn(colName), heading, width);
    }

    public static void setTableDefaults(JTable table) {
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setRowHeight(30);
    }
}
