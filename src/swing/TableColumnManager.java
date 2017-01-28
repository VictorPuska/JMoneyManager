package swing;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by VJP on 8/01/2017.
 */
public class TableColumnManager {

    private JTable clientTable;
    private JTable dlgTable;
    private TableColumn[] baseColumns;

    public TableColumnManager(JTable table) {
        this.clientTable = table;
        baseColumns = new TableColumn[table.getColumnCount()];
        for (int j = 0; j < table.getColumnCount(); j++)
            baseColumns[j] = table.getColumnModel().getColumn(j);
    }

    public void showDialog() {
        JDialog dialog = initDialog();
        dialog.setVisible(true);
    }

    private JDialog initDialog() {
        JDialog dialog = new JDialog(new Frame(), "Columns", true);
        dialog.setAlwaysOnTop(true);
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(null);

        TableColumnModel tcm = new DefaultTableColumnModel();
        tcm.addColumn(new TableColumn(0, 45));
        tcm.addColumn(new TableColumn(1, 150));

        Object[][] dlgData = new Object[baseColumns.length][2];
        for (int j = 0; j < baseColumns.length; j++) {
            int midx = baseColumns[j].getModelIndex();
            dlgData[j][0] = clientTable.convertColumnIndexToView(midx) >= 0;
            dlgData[j][1] = baseColumns[j].getHeaderValue().toString();
        }

        DefaultTableModel dm = new DefaultTableModel(dlgData, new String[]{"Selected", "Column Name"}) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0 ? Boolean.class : String.class);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0;
            }
        };

        dlgTable = new JTable(dm, tcm);
        dlgTable.setRowSelectionAllowed(false);
        dlgTable.setColumnSelectionAllowed(false);
        dlgTable.setBorder(null);
        dlgTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        dlgTable.setFillsViewportHeight(true);
        dlgTable.setBackground(new Color(204, 204, 204));
        dlgTable.setRowHeight(24);
        dlgTable.setPreferredScrollableViewportSize(new Dimension(300, 200));
        dlgTable.getTableHeader().setUI(null);

        scrollPane.setViewportView(dlgTable);
        contentPane.add(scrollPane);
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getOwner());
        return dialog;
    }

    public void updateColumns() {
        for (int j = 0; j < dlgTable.getRowCount(); j++) {
            int midx = baseColumns[j].getModelIndex();
            int vidx = clientTable.convertColumnIndexToView(midx);
            boolean b0 = vidx >= 0;
            boolean b1 = (boolean) dlgTable.getModel().getValueAt(j, 0);
            if (b0 && !b1)
                clientTable.removeColumn(clientTable.getColumnModel().getColumn(vidx));
            if (b1 && !b0)
                clientTable.addColumn(baseColumns[j]);
        }
    }
    public void hideColumn(int modelIndex) {
        for(TableColumn tc: baseColumns) {
            if (tc.getModelIndex() == modelIndex)
                clientTable.removeColumn(tc);
        }
    }

    public String copyColumnViewToString() {
        String s = "";
        for(int j = 0; j<clientTable.getColumnCount(); j++) {
            int midx = clientTable.convertColumnIndexToModel(j);
            int wdth = clientTable.getColumnModel().getColumn(j).getWidth();
            s += String.format("%02d %04d ", midx, wdth);
        }
        return s;
    }

    public void copyStringToColumnView(String columnData) {
        int j = 0;
        TableColumnModel tcm = new DefaultTableColumnModel();
        while(j < columnData.length()) {
            int midx = Integer.valueOf(columnData.substring(j, j+2));
            int wdth = Integer.valueOf(columnData.substring(j+3, j+7));
            for(TableColumn tc: this.baseColumns)
                if(tc.getModelIndex() == midx) {
                    tc.setPreferredWidth(wdth);
                    tcm.addColumn(tc);
                }
            j += 8;
        }
        this.clientTable.setColumnModel(tcm);
    }
}
