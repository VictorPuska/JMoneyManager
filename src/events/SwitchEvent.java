package events;

import java.util.EventObject;

/**
 * Created by VJP on 2/01/2017.
 */
public class SwitchEvent extends EventObject {

    private boolean cancelled = false;

    public SwitchEvent(Object source) {
        super(source);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
