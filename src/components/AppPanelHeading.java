/*
 * Created by JFormDesigner on Sun Oct 09 14:31:25 AEDT 2016
 */

package components;

import java.awt.*;
import javax.swing.*;

/**
 * @author Victor Puska
 */
public class AppPanelHeading extends JPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel headingText;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public AppPanelHeading() {
        initComponents();
    }

    public String getHeadingText() {
        return headingText.getText();
    }

    public void setHeadingText(String headingText) {
        this.headingText.setText(headingText);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        headingText = new JLabel();

        //======== this ========
        setBackground(new Color(51, 51, 255));
        setBorder(null);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        //---- headingText ----
        headingText.setText("Dummy");
        headingText.setFont(new Font("sansserif", Font.PLAIN, 24));
        headingText.setForeground(Color.white);
        add(headingText);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
