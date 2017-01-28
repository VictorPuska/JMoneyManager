package events;

import java.util.EventListener;

/**
 * Created by VJP on 2/01/2017.
 */
public interface SwitchListener extends EventListener {

    public void leavePage(SwitchEvent e);

    public void closePage(SwitchEvent e);

    public void activatePage(SwitchEvent e);
}
