package de.tubs.pandemieinc.events;

import java.util.Map;
import de.tubs.pandemieinc.events.BaseEvent;

public class OtherEvent extends BaseEvent {

    // Use a Map/Dictionary to map the fields of the event that
    // we do not know for the current.
    public static String eventName = "otherEvent";
    public Map<String, Object> fields;

    public OtherEvent(Map<String, Object> fields) {
    	super(eventName);
        this.fields = fields;
    }
}
