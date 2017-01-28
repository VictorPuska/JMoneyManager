package components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by VJP on 18/09/2016.
 */
public class AppPanelManager extends JPanel {

    private static AppPanelManager manager = null;

    //private ArrayList<AppPanel> apps = new ArrayList<>();

    public AppPanelManager() {
        this.setBorder(null);
        this.setLayout(new BorderLayout());
        manager = this;
    }

    public static AppPanelManager getManager() {
        return manager;
    }

    public void gotoApp(AppPanel app) {
        closePanels();
        System.out.println(this.getComponentCount());
        if(this.getComponentCount() ==0)
            this.setCurrent(app);
    }

    public void drillDown(AppPanel app) {
        if (this.getCurrent().deactivate()) {
            this.getCurrent().setVisible(false);
            this.setCurrent(app);
        }
    }

    public void drillUp() {
        if (this.getCurrent().close())
            this.removeCurrent();
        this.getCurrent().setVisible(true);
        this.getCurrent().activate();
    }

    public void closePanels() {
        while(this.getComponentCount() > 0) {
            if(!this.getCurrent().close())
                return;
            this.removeCurrent();
        }
    }

    private AppPanel getCurrent() {
        int j = this.getComponents().length - 1;
        return (AppPanel) this.getComponent(j);
    }

    private void removeCurrent() {
        this.remove(this.getComponentCount()-1);
    }

    private void setCurrent(AppPanel app) {
        this.add(app, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        app.activate();
    }
}
