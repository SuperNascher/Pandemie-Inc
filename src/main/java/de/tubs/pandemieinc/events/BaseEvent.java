package de.tubs.pandemieinc.events;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the base class for all given events. The eventName attribute will be used to know the
 * event type of a given event object. (The idea is the string pointer to the static string location
 * of the implementations)
 */
public abstract class BaseEvent {

    @JsonProperty("type")
    public String eventName;

    public BaseEvent(String eventName) {
        this.eventName = eventName;
    }
}
