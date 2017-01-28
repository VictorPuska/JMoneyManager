package components;

import swing.TableColumnManager;
import zz.DbTableRow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

/**
 * Created by VJP on 28/01/2017.
 */
public class BaseTablePage extends AppPanel {

    private JTable table = null;
    private DefaultTableModel model = null;
    private TableColumnManager tcManager = null;

    private void intialiseRow(DbTableRow row) {
    }

    protected void newActionHandler(ActionEvent e) {
    }

    protected void deleteActionHandler(ActionEvent e) {
    }

    protected void columnHeaderActionHandler(ActionEvent e) {
        if(tcManager != null) {
            tcManager.showDialog();
            tcManager.updateColumns();
        }
    }

}
