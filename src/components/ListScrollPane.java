/*
 * Created by JFormDesigner on Sat Jul 09 14:16:44 AEST 2016
 */

package components;

import java.awt.*;
import javax.swing.*;
import com.jformdesigner.annotations.*;

/**
 * @author Victor Puska
 */
@BeanInfo(isContainer=true,containerDelegate="getClientPane")
public class ListScrollPane extends JPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JScrollPane scrollPane1;
    private ListScrollPaneClient listScrollPaneClient1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public ListScrollPane() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        scrollPane1 = new JScrollPane();
        listScrollPaneClient1 = new ListScrollPaneClient();

        //======== this ========
        setLayout(new BorderLayout());

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(null);

            //======== listScrollPaneClient1 ========
            {
                listScrollPaneClient1.setBorder(null);
                listScrollPaneClient1.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
            }
            scrollPane1.setViewportView(listScrollPaneClient1);
        }
        add(scrollPane1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public Container getClientPane() {
        return this.listScrollPaneClient1;
    }
}
