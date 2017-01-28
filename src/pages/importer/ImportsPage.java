/*
 * Created by JFormDesigner on Sat Dec 31 12:09:54 AEDT 2016
 */

package pages.importer;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import javax.rmi.CORBA.Util;
import javax.swing.*;

import application.Application;
import application.Utilities;
import components.*;
import application.Database;
import pages.transactions.TransactionsPage;
import swing.TableColumnManager;
import zz.DbTable;
import zz.DbTableRowSet;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 * @author Victor Puska
 */
public class ImportsPage extends AppPanel {

    //region JForms Designer Generated Code
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private AppPanelHeading appPanelHeading1;
    private JPanel toolBar;
    private JButton btnDelete;
    private JButton btnColumns;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        appPanelHeading1 = new AppPanelHeading();
        toolBar = new JPanel();
        btnDelete = new JButton();
        btnColumns = new JButton();

        //======== this ========
        setLayout(new BorderLayout());

        //---- appPanelHeading1 ----
        appPanelHeading1.setHeadingText("Imports");
        add(appPanelHeading1, BorderLayout.NORTH);

        //======== toolBar ========
        {
            toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

            //---- btnDelete ----
            btnDelete.setText("Delete");
            btnDelete.addActionListener(e -> btnDeleteActionPerformed(e));
            toolBar.add(btnDelete);

            //---- btnColumns ----
            btnColumns.setText("Columns");
            btnColumns.addActionListener(e -> btnColumnsActionPerformed(e));
            toolBar.add(btnColumns);
        }
        add(toolBar, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    //endregion

    JTable table;
    DbTableRowSet model;
    TableColumnModel tcm;
    TableColumnManager tcManager;

    public ImportsPage() {
        initComponents();

        try {
            DbTable dbTable = new DbTable(Database.conn, "IMPORTS");
            dbTable.addColumn("ACCOUNT_NAME", Types.VARCHAR);
            dbTable.addColumn("MIN_DATE", Types.DATE);
            dbTable.addColumn("MAX_DATE", Types.DATE);
            dbTable.addColumn("COUNT_TRAN", Types.INTEGER);
            model = new DbTableRowSet(dbTable);
            model.fill(dbTable.selectAll("V_IMPORTS"));
        }
        catch (SQLException e) {
            Application.fatalError(e);
        }
        //tcm = new JmmTableColumnModel("IMPORTS_TCM", "00 0100,01 0150,02 0300,03 0100,04 0100,05 0100,");

        tcm = new DefaultTableColumnModel();
        tcm.addColumn(Utilities.createTableColumn(model, "IMPORT_DATETIME", "Date", 100 ));
        tcm.addColumn(Utilities.createTableColumn(model, "ACCOUNT_NAME", "Account", 300));
        tcm.addColumn(Utilities.createTableColumn(model, "MIN_DATE", "From", 50));
        tcm.addColumn(Utilities.createTableColumn(model, "MAX_DATE", "To", 50));
        tcm.addColumn(Utilities.createTableColumn(model, "COUNT_TRAN", "Count", 50));
        tcm.getColumn(0).setCellRenderer(new DateTimeRenderer());

        table = new JTable(model, tcm);
        Utilities.setTableDefaults(table);
        this.tcManager = new TableColumnPersister(table, this, "BBBZ");

        table.setRowSorter(new ImportsPage.TableSorter(model));

        JScrollPane pane = new JScrollPane(table);
        this.add(pane, BorderLayout.CENTER );

        table.addMouseListener(new MouseEventListener());
    }

    private class TableSorter extends TableRowSorter<DbTableRowSet> {
        TableSorter(DbTableRowSet model) {
            super(model);
        }

        @Override
        public void rowsInserted(int firstRow, int endRow) {
            super.rowsInserted(firstRow, endRow);
            table.setRowSelectionInterval(firstRow, firstRow);
        }
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

    private class MouseEventListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getClickCount()==2 && e.getButton()==MouseEvent.BUTTON1) {
                int row = table.convertRowIndexToModel(table.rowAtPoint(e.getPoint()));
                String acc = (String) model.getRow(row).getColumn("ACCOUNT_NAME");
                JOptionPane.showMessageDialog(table, acc, "Double Click", JOptionPane.INFORMATION_MESSAGE);
            }
            AppPanelManager.getManager().drillDown(new TransactionsPage());
        }
    }

    private void btnColumnsActionPerformed(ActionEvent e) {
        tcManager.showDialog();
        tcManager.updateColumns();
    }

    private void btnDeleteActionPerformed(ActionEvent e) {
        // TODO add your code here
        int j = table.getSelectedRowCount();
        if (j > 0) {
            for (int i = 0; i < j; i++) {
                int rowno = table.getSelectedRows()[i];
                int rownom = table.convertRowIndexToModel(rowno);
                model.deleteRow(rownom);
            }
        }
    }

}
