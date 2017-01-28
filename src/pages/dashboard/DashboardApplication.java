/*
 * Created by JFormDesigner on Fri Jul 29 20:39:45 AEST 2016
 */

package pages.dashboard;

import java.awt.*;
import javax.swing.*;
import components.*;

/**
 * @author Victor Puska
 */
public class DashboardApplication extends AppPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JTextField textField1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public DashboardApplication() {
        System.out.println("New Dashboard");
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        textField1 = new JTextField();

        //======== this ========
        setLayout(new BorderLayout());

        //---- textField1 ----
        textField1.setText("This is the dashboard");
        textField1.setBorder(null);
        add(textField1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    @Override
    public void activate() {
        super.activate();
        System.out.println("Activate dashboard");
    }
}
