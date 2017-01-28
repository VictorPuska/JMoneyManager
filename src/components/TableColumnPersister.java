package components;

import application.Settings;
import events.SwitchEvent;
import events.SwitchListener;
import swing.TableColumnManager;

import javax.swing.*;

/**
 * Created by VJP on 26/01/2017.
 */
public class TableColumnPersister extends TableColumnManager implements SwitchListener {

    private String identifier;

    public TableColumnPersister(JTable table, AppPanel appPanel, String identifier) {
        super(table);
        appPanel.addSwitchListener(this);
        this.identifier = identifier;
    }

    @Override
    public void leavePage(SwitchEvent e) {
        System.out.println("Leaving page");
        Settings.putSetting(this.identifier, this.copyColumnViewToString(), 0);
    }

    @Override
    public void closePage(SwitchEvent e) {
        System.out.println("closing page");
        Settings.putSetting(this.identifier, this.copyColumnViewToString(), 0);
    }

    @Override
    public void activatePage(SwitchEvent e) {
        System.out.println("activating page");
        String s = Settings.getSetting(this.identifier, null);
        if(s != null)
            this.copyStringToColumnView(s);
    }
}
