/*
 * Created by JFormDesigner on Sat Jan 28 13:45:25 AEDT 2017
 */

package pages.transactions;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import javax.rmi.CORBA.Util;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;

import application.Application;
import application.Database;
import application.Utilities;
import components.*;
import pages.accounts.AccountApplication;
import swing.TableColumnManager;
import zz.DbTable;
import zz.DbTableRowSet;

/**
 * @author Victor Puska
 */
public class TransactionsPage extends AppPanel {

    //region JForms Designer Generated Code
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private AppPanelHeading appPanelHeading1;
    private JPanel toolBar;
    private JButton btnColumns;
    private JScrollPane scrollPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

     private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        appPanelHeading1 = new AppPanelHeading();
        toolBar = new JPanel();
        btnColumns = new JButton();
        scrollPane1 = new JScrollPane();

        //======== this ========
        setLayout(new BorderLayout());

        //---- appPanelHeading1 ----
        appPanelHeading1.setHeadingText("Transactions");
        add(appPanelHeading1, BorderLayout.NORTH);

        //======== toolBar ========
        {
            toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

            //---- btnColumns ----
            btnColumns.setText("Columns");
            btnColumns.addActionListener(e -> btnColumnsActionPerformed(e));
            toolBar.add(btnColumns);
        }
        add(toolBar, BorderLayout.SOUTH);
        add(scrollPane1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    //endregion

    private JTable table;
    private TableColumnManager tcManager;
    private DbTableRowSet model;

    public TransactionsPage() {
        initComponents();

        try {
            DbTable t = new DbTable(Database.conn, "V_TRANSACTIONS");
            model = new DbTableRowSet(t);
            model.fill();
            model.setViewMode(true);
        } catch (SQLException e) {
            Application.fatalError(e);
        }

        TableColumnModel tcm = new DefaultTableColumnModel();
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_ID",          "Trn.Id.",      50));
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_SOURCE_DATA", "Source",      200));
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_DATE",        "Date",         50));
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_DESCRIPTION", "Memo",         75));
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_CARDHOLDER",  "Card Holder",  100));
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_DUPLICATE",   "Duplicate",    50));
        tcm.addColumn(Utilities.createTableColumn(model, "TRANSACTION_AMOUNT",      "Amount",       50));
        tcm.addColumn(Utilities.createTableColumn(model, "IMPORT_UID",              "Import Id",    50));
        tcm.addColumn(Utilities.createTableColumn(model, "IMPORT_DATETIME",         "Import Date",  50));
        tcm.addColumn(Utilities.createTableColumn(model, "IMPORT_TYPE",             "Import Type",  50));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_UID",             "A/c Id",       50));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_NAME",            "A/c Name",     200));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_ACTIVE",          "A/c Active",   50));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_TYPE",            "A/c Type",     50));
        tcm.getColumn(8).setCellRenderer(new DateTimeRenderer());

        Utilities.setTableDefaults(table);

        //this.tcManager = new TableColumnPersister(table, this, "AAAZ");
        //table.setRowSorter(new AccountApplication.TableSorter(model));
        this.scrollPane1.getViewport().add(table);
    }

    static class DateTimeRenderer extends DefaultTableCellRenderer {
        DateFormat formatter;
        public DateTimeRenderer() { super(); }

        public void setValue(Object value) {
            if (formatter==null) {
                formatter = DateFormat.getDateTimeInstance();
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }


    private void btnColumnsActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

}
