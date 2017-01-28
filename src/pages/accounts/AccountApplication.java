/*
 * Created by JFormDesigner on Sat Aug 20 13:45:22 AEST 2016
 */

package pages.accounts;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import javax.swing.*;
import javax.swing.table.*;

import application.Application;
import application.Utilities;
import components.*;
import application.Database;
import swing.TableColumnManager;
import zz.DbTable;
import zz.DbTableRow;
import zz.DbTableRowSet;

/**
 * @author Victor Puska
 */
public class AccountApplication extends AppPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JToolBar toolBar;
    private JButton btnNew;
    private JButton btnDelete;
    private JButton btnColumns;
    private AppPanelHeading appPanelHeading1;
    private JScrollPane scrollPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    DbTableRowSet model;
    JTable table;
    TableColumnManager tcManager;

    public AccountApplication() {
        initComponents();
        try {
            DbTable t = new DbTable(Database.conn, "ACCOUNTS");
            t.addColumn("_BALANCE", Types.DECIMAL);
            model = new DbTableRowSet(t);
            model.fill(t.selectAll("(SELECT ACCOUNT_UID, ACCOUNT_NAME, 0.00 AS _BALANCE, ACCOUNT_ACTIVE FROM ACCOUNTS)"));
        } catch (SQLException e) {
            Application.fatalError(e);
        }

        TableColumnModel tcm = new DefaultTableColumnModel();
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_UID",     "Id", 50));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_NAME",    "Name", 400));
        tcm.addColumn(Utilities.createTableColumn(model, "_BALANCE",        "Balance", 100));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_ACTIVE",  "Active", 75));

        this.table = new JTable(model, tcm);
        Utilities.setTableDefaults(table);
        this.tcManager = new TableColumnPersister(table, this, "AAAZ");
        table.setRowSorter(new TableSorter(model));
        this.scrollPane1.getViewport().add(table);
    }


    protected void newActionHandler(ActionEvent e) {
        table.clearSelection();
        DbTableRow r = model.newRow();
        r.setColumn("ACCOUNT_NAME", "This is a new account");
        r.setColumn("_BALANCE", "-456.25");
        model.addRow(r);
    }

    private void btnDeleteClick(ActionEvent e) {
        int[] rows = table.getSelectedRows();
        for(int j=0; j<rows.length; j++)
            rows[j] = table.convertRowIndexToModel(rows[j]);
        for(int j=0; j<rows.length; j++) {
            model.deleteRow(rows[j]);
            for(int k=j; k<rows.length; k++)
                if(rows[k] > rows[j])
                    rows[k] -= 1;
        }
    }


    private void columnHeaderActionHandler(ActionEvent e) {
        tcManager.showDialog();
        tcManager.updateColumns();
    }


    private class TableSorter extends TableRowSorter<DbTableRowSet> {
        TableSorter(DbTableRowSet model) {
            super(model);
        }

        @Override
        public void rowsInserted(int firstRow, int endRow) {
            super.rowsInserted(firstRow, endRow);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JScrollPane p = (JScrollPane) table.getParent().getParent();
                    table.getSelectionModel().setSelectionInterval(firstRow, endRow);
                    p.getVerticalScrollBar().setValue(
                            p.getVerticalScrollBar().getMaximum() );
                 }
            });
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        toolBar = new JToolBar();
        btnNew = new JButton();
        btnDelete = new JButton();
        btnColumns = new JButton();
        appPanelHeading1 = new AppPanelHeading();
        scrollPane1 = new JScrollPane();

        //======== this ========
        setBorder(null);
        setLayout(new BorderLayout());

        //======== toolBar ========
        {
            toolBar.setFloatable(false);

            //---- btnNew ----
            btnNew.setText("New");
            btnNew.addActionListener(e -> newActionHandler(e));
            toolBar.add(btnNew);

            //---- btnDelete ----
            btnDelete.setText("Delete");
            btnDelete.addActionListener(e -> btnDeleteClick(e));
            toolBar.add(btnDelete);

            //---- btnColumns ----
            btnColumns.setText("Columns");
            btnColumns.addActionListener(e -> columnHeaderActionHandler(e));
            toolBar.add(btnColumns);
        }
        add(toolBar, BorderLayout.SOUTH);

        //---- appPanelHeading1 ----
        appPanelHeading1.setHeadingText("Accounts");
        add(appPanelHeading1, BorderLayout.NORTH);
        add(scrollPane1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
