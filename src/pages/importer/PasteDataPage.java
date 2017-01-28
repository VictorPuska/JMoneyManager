/*
 * Created by JFormDesigner on Wed Sep 21 18:42:57 AEST 2016
 */

package pages.importer;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import application.Application;
import components.*;
import application.Database;
import pages.home.HomeApplication;
import zz.DbTable;
import zz.DbTableRow;

/**
 * @author Victor Puska
 */
public class PasteDataPage extends AppPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel toolBar;
    private JButton btnPaste;
    private JButton btnNext;
    private JPanel hSpacer1;
    private JLabel label1;
    private JComboBox comboAccount;
    private AppPanelHeading appPanelHeading1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    ImportTable table;
    PastedDataModel model;
    //ImportColumnModel tcm;

    public PasteDataPage() {
        initComponents();
        // Load accounts table
        try {
            DbTable accTbl = new DbTable(Database.conn, "ACCOUNTS");
            for(DbTableRow r: accTbl.selectWhere("ACCOUNT_ACTIVE=TRUE"))
                comboAccount.addItem(new AccountRow(r));
        }
        catch (SQLException e) {
            Application.fatalError(e);
        }
    }

    private class ImportTable extends JTable {

        private ImportTable(PastedDataModel model) {
            super(model);
            this.setDefaultRenderer(Object.class, new MyTableCellRenderer());
            for(int j = 0; j < model.getColumnCount(); j++) {
                int w = 0;
                for (int k = 0; k < model.getRowCount(); k++) {
                    if(model.getValueAt(k,j).toString().length() > w)
                        w = model.getValueAt(k,j).toString().length();
                }
                TableColumn tc = this.getColumnModel().getColumn(j);
                tc.setHeaderValue("Skip");
                tc.setPreferredWidth(w*8+12);
                //this.setColumnMenu(j, new ColumnMenu(j));
            }
        }

    }

    /**
     * <code>MyTableCellRenderer</code> defines a cell renderer for the paste data <code>KTable</code>.  This cell
     * renderer will render any cells that have an error in pink.
     */
    private class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            PastedDataModel model = (PastedDataModel) table.getModel();
            // restore default background colour
            this.setBackground(null);
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(model.getErrorAt(row, table.convertColumnIndexToModel(column)))
                c.setBackground(Color.PINK);
            return c;
        }
    }

    /**
     * <code>ColumnMenu</code> defines a <code>JPopupMenu</code> for the table's column headers.  The menu
     * allows the user to nominate what field the column represents.  The calling program creates a difference
     * <code>ColumnMenu</code> for each column.  A private field - <code>column</code> - defines which column
     * an instance is attached to.
     */
    private class ColumnMenu extends JPopupMenu {

        private int column;
        
        private ColumnMenu(int column) {
            super();
            this.column = column;
            this.add(new Item("Skip"));
            this.add(new JPopupMenu.Separator());
            this.add(new Item("Date"));
            this.add(new Item("Memo"));
            this.add(new Item("Card Holder"));
            this.add(new Item("Amount +"));
            this.add(new Item("Amount -"));
        }

        /**
         * <code>Item</code> is a <code>JMenuItem</code> with a specialised <code>ActionListener</code>.
         */
        private class Item extends JMenuItem implements ActionListener {

            Item(String text) {
                super(text);
                this.addActionListener(this);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getText() + " Col:" + ColumnMenu.this.column);
                ColumnMenu.this.setVisible(false);
                String newHeading = this.getText();
                int viewColumn = table.convertColumnIndexToView(column);
                boolean hasDateCol = false;
                boolean hasAmouCol = false;
                boolean hasMemoCol = false;

                for(int j=0; j < table.getColumnCount(); j++) {
                    TableColumn tc = table.getColumnModel().getColumn(j);
                    String oldHeading = tc.getHeaderValue().toString();
                    if (oldHeading.substring(0, 4).equals(newHeading.substring(0, 4)))
                        tc.setHeaderValue("Skip");
                    if (j == viewColumn)
                        tc.setHeaderValue(newHeading);
                    int modelColumn = table.convertColumnIndexToModel(j);
                    String s = tc.getHeaderValue().toString();
                    PasteDataPage.this.model.setColumnType(modelColumn, s);
                    if(s.equals("Date"))
                        hasDateCol = true;
                    if(s.equals("Memo"))
                        hasMemoCol = true;
                    if(s.substring(0,4).equals("Amou"))
                        hasAmouCol = true;
                }
                table.getTableHeader().repaint();
                PasteDataPage.this.model.fireTableDataChanged();
                btnNext.setEnabled(hasDateCol & hasMemoCol & hasAmouCol
                        & !PasteDataPage.this.model.hasErrors()
                        & (comboAccount.getSelectedItem() != null));
            }
        }
    }

    private void btnPasteClick(ActionEvent e) {
        model = new PastedDataModel();
        table = new ImportTable(model);
        JScrollPane pane = new JScrollPane(table);
        this.add(pane, BorderLayout.CENTER);
        this.revalidate();
    }

    private void btnNextClick(ActionEvent e) {
        //AppPanelManager.getManager().drillDown(new PasteDataAccount());
        AccountRow account = (AccountRow) comboAccount.getSelectedItem();
        int count = this.model.saveData((int) account.row.getColumn("ACCOUNT_UID"));
        JOptionPane.showMessageDialog(
                new JFrame(),
                "Transactions inserted = " + count,
                "Money.Manger Import Complete",
                JOptionPane.INFORMATION_MESSAGE);
        AppPanelManager.getManager().gotoApp(new HomeApplication());
    }

    private class AccountRow {

        DbTableRow row;

        AccountRow(DbTableRow row) {
            this.row = row;
        }

        @Override
        public String toString() {
            return (String) row.getColumn("ACCOUNT_NAME");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        toolBar = new JPanel();
        btnPaste = new JButton();
        btnNext = new JButton();
        hSpacer1 = new JPanel(null);
        label1 = new JLabel();
        comboAccount = new JComboBox();
        appPanelHeading1 = new AppPanelHeading();

        //======== this ========
        setBorder(null);
        setLayout(new BorderLayout());

        //======== toolBar ========
        {
            toolBar.setBorder(null);
            toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

            //---- btnPaste ----
            btnPaste.setText("Paste");
            btnPaste.addActionListener(e -> btnPasteClick(e));
            toolBar.add(btnPaste);

            //---- btnNext ----
            btnNext.setText("NEXT");
            btnNext.setEnabled(false);
            btnNext.addActionListener(e -> btnNextClick(e));
            toolBar.add(btnNext);
            toolBar.add(hSpacer1);

            //---- label1 ----
            label1.setText("Account:");
            label1.setBorder(null);
            toolBar.add(label1);

            //---- comboAccount ----
            comboAccount.setBorder(null);
            toolBar.add(comboAccount);
        }
        add(toolBar, BorderLayout.SOUTH);

        //---- appPanelHeading1 ----
        appPanelHeading1.setHeadingText("Import");
        add(appPanelHeading1, BorderLayout.NORTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

}
