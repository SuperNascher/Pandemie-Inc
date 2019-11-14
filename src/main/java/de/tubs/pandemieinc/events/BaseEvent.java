package de.tubs.pandemieinc.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseEvent {

    @JsonProperty("type")
    public String eventName;

    public BaseEvent(String eventName) {
        this.eventName = eventName;
    }
}
