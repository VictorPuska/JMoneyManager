/*
 * Created by JFormDesigner on Sun Oct 09 14:02:51 AEDT 2016
 */

package pages.home;

import java.awt.event.*;
import javax.swing.*;

import pages.accounts.AccountApplication;
import pages.importer.ImportsPage;
import pages.importer.PasteDataPage;
import components.*;

/**
 * @author Victor Puska
 */
public class HomeApplication extends AppPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    private JButton buttonAccounts;
    private JButton buttonImport;
    private JButton buttonImports;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public HomeApplication() {
        initComponents();
    }

    private void accountsClick(ActionEvent e) {
        // TODO add your code here
        AppPanelManager.getManager().gotoApp(new AccountApplication());
    }

    private void buttonImportClick(ActionEvent e) {
        // TODO add your code here
        AppPanelManager.getManager().gotoApp(new PasteDataPage());
    }

    private void buttonImportsClick(ActionEvent e) {
        // TODO add your code here
        AppPanelManager.getManager().gotoApp(new ImportsPage());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        buttonAccounts = new JButton();
        buttonImport = new JButton();
        buttonImports = new JButton();

        //======== this ========

        //---- label1 ----
        label1.setText("This is the home page");

        //---- buttonAccounts ----
        buttonAccounts.setText("Accounts");
        buttonAccounts.addActionListener(e -> accountsClick(e));

        //---- buttonImport ----
        buttonImport.setText("Import");
        buttonImport.addActionListener(e -> buttonImportClick(e));

        //---- buttonImports ----
        buttonImports.setText("Imports");
        buttonImports.addActionListener(e -> buttonImportsClick(e));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(52, 52, 52)
                    .addGroup(layout.createParallelGroup()
                        .addComponent(label1)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                            .addComponent(buttonImports, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonImport, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonAccounts, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap(380, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addComponent(label1)
                    .addGap(35, 35, 35)
                    .addComponent(buttonAccounts)
                    .addGap(18, 18, 18)
                    .addComponent(buttonImport)
                    .addGap(18, 18, 18)
                    .addComponent(buttonImports)
                    .addContainerGap(177, Short.MAX_VALUE))
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
