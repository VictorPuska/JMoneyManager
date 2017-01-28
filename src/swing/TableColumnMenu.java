package swing;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by VJP on 4/08/2015.
 *
 * Persistent column stuff
 */
class TableColumnMenu {

    private JTableHeader header;                        // JTable's column header object
    private int modelColumn;                            // Model column we are managing the menu for
    private JPopupMenu popupMenu;                      // JPopupMenu we delegate the low level work to

    private TableHeaderMouseListener headerMouseListener = new TableHeaderMouseListener();

    // This mouse exit handler is added to the JPopupMenu and its menuItems to detect when the
    // mouse leaves the menu and the menu should be closed.
    private MouseAdapter menuMouseExitListener = new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            TableColumnMenu m = TableColumnMenu.this;
            if(popupMenu.isVisible())
                m.setVisible(m.getScreenArea().contains(e.getLocationOnScreen()));
            else
                TableColumnMenu.this.setVisible(false);
        }
    };

    private MouseAdapter itemMouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            JComponent item = (JComponent) e.getSource();
            if (item instanceof JMenuItem) {
                JMenuItem menu = (JMenuItem) item;
                menu.setArmed(true);
            }
        }
        @Override
        public void mouseExited(MouseEvent e) {
            JComponent item = (JComponent) e.getSource();
            if (item instanceof JMenuItem) {
                JMenuItem menu = (JMenuItem) item;
                menu.setArmed(false);
            }
            TableColumnMenu m = TableColumnMenu.this;
            m.setVisible(m.getScreenArea().contains(e.getLocationOnScreen()));
        }

    };

    TableColumnMenu(JTableHeader header, int modelColumn, JPopupMenu menu) {
        this.header = header;
        this.modelColumn = modelColumn;
        this.popupMenu = ( menu==null ? new JPopupMenu() : menu);
        //this.popupMenu.addMouseListener(menuMouseExitListener);
        this.header.addMouseListener(headerMouseListener);
    }

    JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    void setPopupMenu(JPopupMenu menu) {
        this.popupMenu = menu;
    }

    public int getModelColumn() { return this.modelColumn; };

    void remove() {
        this.header.removeMouseListener(headerMouseListener);
    }

    boolean isVisible() {
        return popupMenu.isVisible();
    }

    void setVisible(boolean visible) {
        if (visible == popupMenu.isVisible())
            return;
        popupMenu.setVisible(visible);
        if(visible) {
            // add the motion and exit listeners to our JPopupMenu
            this.popupMenu.addMouseListener(menuMouseExitListener);
            header.addMouseMotionListener(headerMouseListener);
            for(Component c: popupMenu.getComponents())
                c.addMouseListener(itemMouseListener);
        }
        else {
            // remove the motion and exit listeners from out JPopupMenu
            this.popupMenu.removeMouseListener(menuMouseExitListener);
            header.removeMouseMotionListener(headerMouseListener);
            for(Component c: popupMenu.getComponents())
                c.removeMouseListener(itemMouseListener);
        }
    }

    private Rectangle getScreenArea() {
        // return the Rectangle on screen occupied by the JPopupMenu
        Point locn = popupMenu.getLocationOnScreen();
        return new Rectangle(locn.x, locn.y, popupMenu.getWidth(), popupMenu.getHeight());
    }

    private Rectangle getHeaderArea() {
        // return the Rectangle on screen occupied by our column's header.  Note that this
        // is narrowed by 5 pixels on the left and right margin to allow for JTable's automatic
        // behaviour regarding column resizing.
        Point location = header.getLocationOnScreen();
        Rectangle colrect = header.getHeaderRect(header.getTable().convertColumnIndexToView(modelColumn));
        colrect.translate(location.x, location.y);
        colrect.x += 5;
        colrect.width -= 10;
        return colrect;
    }


    /********************************************************************************************
     * Listener for this menu's table menu
     ********************************************************************************************/
    private class TableHeaderMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            Point point  = event.getPoint();
            JTable table = TableColumnMenu.this.header.getTable();
            int vcolumn  = table.columnAtPoint(point);
            int mcolumn  = table.convertColumnIndexToModel(vcolumn);

            // we are only interested in RMB clicks for our columns
            if (!(SwingUtilities.isRightMouseButton(event)
                    && (mcolumn == TableColumnMenu.this.modelColumn)))
                return; // do nothing and get out

            // closing menu..
            if (TableColumnMenu.this.isVisible()) {
                TableColumnMenu.this.setVisible(false);
                return;
            }

            Point location = TableColumnMenu.this.header.getLocationOnScreen();
            Rectangle colrect = TableColumnMenu.this.header.getHeaderRect(vcolumn);
            location.translate(colrect.x, colrect.y + header.getHeight());
            TableColumnMenu.this.popupMenu.setLocation(location);
            TableColumnMenu.this.setVisible(true);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (TableColumnMenu.this.isVisible()) {
                Point p = e.getLocationOnScreen();
                Rectangle rh = getHeaderArea();
                TableColumnMenu.this.setVisible(rh.contains(p));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (TableColumnMenu.this.isVisible()) {
                Point p = e.getLocationOnScreen();
                boolean visible = TableColumnMenu.this.getHeaderArea().contains(p)
                                | TableColumnMenu.this.getScreenArea().contains(p);
                TableColumnMenu.this.setVisible(visible);
            }
        }
    }
}