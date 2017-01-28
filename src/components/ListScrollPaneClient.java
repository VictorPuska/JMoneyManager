/*
 * Created by JFormDesigner on Sat Jul 09 14:10:05 AEST 2016
 */

package components;

import java.awt.*;
import javax.swing.*;

/**
 * @author Victor Puska
 */
public class ListScrollPaneClient extends JPanel implements Scrollable {

    public ListScrollPaneClient() {
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        int y = this.getParent().getHeight() - 1;
        for ( Component c: this.getComponents()) {
            int y1 = c.getY() + c.getHeight();
            if (y1 > y)
                y = y1;
        }
        this.setPreferredSize(new Dimension(this.getWidth(), y));
        return this.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int increment = visibleRect.height;
        int componentH = 1;
        if ( this.getComponents().length > 0 )
            componentH = this.getComponents()[0].getHeight();
        return ( increment < componentH ? increment : componentH );
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return visibleRect.height;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
