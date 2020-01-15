package de.tubs.pandemieinc.events;

import java.util.Map;

/**
 * This is a wrapper class to catch an unknown event type for the round. If an event is unknown to
 * the current source code, than this will be used to parse the event into an object and the fields
 * field will be used to parse the attributes of the given event.
 */
public class OtherEvent extends BaseEvent {

    // Use a Map/Dictionary to map the fields of the event that
    // we do not know for the current.
    public static String eventName = "otherEvent";
    public Map<String, String> fields;

    public OtherEvent(Map<String, String> fields) {
        super(eventName);
        this.fields = fields;
    }
}
