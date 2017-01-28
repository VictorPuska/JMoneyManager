package swing;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;

/**
 * <code>JTable</code> with extended functionality. <br>
 *      - table header popup menu support.<br>
 * <br>
 * Created by VJP on 21/10/2016.
 */
public class KTable extends JTable {

    private Hashtable<Integer, TableColumnMenu> columnMenus = new Hashtable();

    /**
     * Create a new <code>KTable</code>.  <br>
     * @param tableModel    The table model for this <code>JTable</code>.
     */
    public KTable(TableModel tableModel) {
        super(tableModel);
    }

    /**
     * Create a new <code>KTable</code>.  <br>
     * @param tableModel    The table model for this <code>JTable</code>.
     * @param columnModel   The table column model for this <code>JTable</code>
     */
    public KTable(TableModel tableModel, TableColumnModel columnModel) {
        super(tableModel, columnModel);
    }

    /**
     * Sets the context menu for the nominated column header.
     * @param modelColumn   The model's column number
     * @param menu          <code>JPopupMenu</code> to use as the context menu.
     *                      If <code>null</code>, the menu is removed.
     */
    public void setColumnMenu(int modelColumn, JPopupMenu menu) {
        if (menu==null) {
            // Treat a call with null JPopupMenu as a remove request
            this.removeColumnMenu(modelColumn);
        } else {
            TableColumnMenu tcm = columnMenus.get(modelColumn);
            if (tcm == null) {
                tcm = new TableColumnMenu(this.getTableHeader(), modelColumn, menu);
                columnMenus.put(modelColumn, tcm);
            } else
                tcm.setPopupMenu(menu);
        }
    }

    /**
     * Returns the <code>JPopupMenu</code> associated with the column header.
     * @param modelColumn   The model's column number
     * @return <code>JPopupMenu</code> associated with the column header
     */
    public JPopupMenu getColumnMenu(int modelColumn) {
        TableColumnMenu tcm = columnMenus.get(modelColumn);
        if(tcm==null) return null; else return tcm.getPopupMenu();
    }

    /**
     * Removes the context menu for the nominated column header.
     * @param modelColumn   The model's column number
     */
    public void removeColumnMenu(int modelColumn) {
        TableColumnMenu tcm = columnMenus.get(modelColumn);
        if(tcm != null) {
            tcm.remove();
            columnMenus.remove(modelColumn);
        }
    }
}
