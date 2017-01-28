package components;

import events.SwitchEvent;
import events.SwitchListener;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Victor Puska
 */
public class AppPanel extends JPanel {

    private ArrayList<SwitchListener> listeners = new ArrayList<SwitchListener>();

    public AppPanel() {
        setBorder(null);
        setBackground(Color.white);
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public static AppPanel getAppPanel(Component comp) {
        if(comp instanceof AppPanel)
            return (AppPanel) comp;
        else
            return getAppPanel(comp.getParent());
    }

    public void activate() {
        ArrayList<SwitchListener> list;
        SwitchEvent e = new SwitchEvent(this);
        synchronized (this) {
            list = (ArrayList<SwitchListener>) this.listeners.clone();
        }
        for(SwitchListener l: list) {
            l.activatePage(e);
        }
    }

    public boolean deactivate() {
        ArrayList<SwitchListener> list;
        SwitchEvent e = new SwitchEvent(this);
        synchronized (this) {
            list = (ArrayList<SwitchListener>) this.listeners.clone();
        }
        System.out.print("deactivate");
        for(SwitchListener l: list) {
            l.leavePage(e);
            if(e.isCancelled())
                return false;
        }
        return true;
    }

    public boolean close() {
        ArrayList<SwitchListener> list;
        SwitchEvent e = new SwitchEvent(this);
        synchronized (this) {
            list = (ArrayList<SwitchListener>) this.listeners.clone();
        }
        for(SwitchListener l: list) {
            l.closePage(e);
            if(e.isCancelled())
                return false;
        }
        return true;
    }

    synchronized public void addSwitchListener(SwitchListener l) {
        listeners.add(l);
    }

    synchronized public void removeSwitchListener(SwitchListener l) {
        listeners.remove(l);
    }
}
