package application;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
/*
 * Created by JFormDesigner on Sat Jun 25 18:00:54 AEST 2016
 */



/**
 * @author Victor Puska
 */
public class InitialFileDialog extends JFrame  {
    public InitialFileDialog() {
        initComponents();

    }

    private void searchButtonMouseClicked(MouseEvent e) {
        JFileChooser fc = new JFileChooser();
        int rtn = 0;
        if (this.existingOption.isSelected())
            rtn = fc.showOpenDialog(this);
        else
            rtn = fc.showSaveDialog(this);

        File f = fc.getSelectedFile();
        fileText.setText(f.getAbsolutePath());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Victor Puska
        mainPanel = new JPanel();
        mmLabel = new JLabel();
        textPane = new JTextPane();
        createOption = new JRadioButton();
        existingOption = new JRadioButton();
        dbIconLabel = new JLabel();
        fileText = new JTextField();
        searchButton = new JButton();
        selectFileLabel = new JLabel();
        okButton = new JButton();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== mainPanel ========
        {
            mainPanel.setBackground(Color.white);
            mainPanel.setFocusable(false);

            // JFormDesigner evaluation mark
            mainPanel.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), mainPanel.getBorder())); mainPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


            //---- mmLabel ----
            mmLabel.setIcon(new ImageIcon(getClass().getResource("/moneymanger.png")));

            //---- textPane ----
            textPane.setText("No money.manager database has be associated with the program.  Please create a database or select an existing one.");
            textPane.setEditable(false);
            textPane.setOpaque(false);
            textPane.setFocusable(false);

            //---- createOption ----
            createOption.setText("Create new database");
            createOption.setSelected(true);

            //---- existingOption ----
            existingOption.setText("Open existing database");

            //---- dbIconLabel ----
            dbIconLabel.setIcon(new ImageIcon(getClass().getResource("/2000px-Applications-database.svg.png")));

            //---- searchButton ----
            searchButton.setText("...");
            searchButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    searchButtonMouseClicked(e);
                }
            });

            //---- selectFileLabel ----
            selectFileLabel.setText("Selected file:");
            selectFileLabel.setFont(selectFileLabel.getFont().deriveFont(selectFileLabel.getFont().getStyle() | Font.BOLD));

            //---- okButton ----
            okButton.setText("Ok");
            okButton.setFont(okButton.getFont().deriveFont(okButton.getFont().getStyle() | Font.BOLD));

            GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
            mainPanel.setLayout(mainPanelLayout);
            mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup()
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup()
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mmLabel))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(okButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(dbIconLabel)
                                        .addGap(30, 30, 30)
                                        .addGroup(mainPanelLayout.createParallelGroup()
                                            .addComponent(textPane, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(createOption)
                                            .addComponent(existingOption)
                                            .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                                        .addComponent(selectFileLabel)
                                                        .addGap(79, 79, 79))
                                                    .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                        .addComponent(fileText, GroupLayout.PREFERRED_SIZE, 387, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                                                .addComponent(searchButton)))))))
                        .addContainerGap(98, Short.MAX_VALUE))
            );
            mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup()
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(mmLabel)
                        .addGap(48, 48, 48)
                        .addGroup(mainPanelLayout.createParallelGroup()
                            .addComponent(dbIconLabel)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(textPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(createOption)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(existingOption)))
                        .addGap(33, 33, 33)
                        .addComponent(selectFileLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup()
                            .addComponent(searchButton)
                            .addComponent(fileText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addComponent(okButton)
                        .addContainerGap(139, Short.MAX_VALUE))
            );
        }
        contentPane.add(mainPanel, BorderLayout.CENTER);
        setSize(745, 475);
        setLocationRelativeTo(null);

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(createOption);
        buttonGroup1.add(existingOption);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Victor Puska
    private JPanel mainPanel;
    private JLabel mmLabel;
    private JTextPane textPane;
    private JRadioButton createOption;
    private JRadioButton existingOption;
    private JLabel dbIconLabel;
    private JTextField fileText;
    private JButton searchButton;
    private JLabel selectFileLabel;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
