package com.tgt.warehouse;

public class EventRecord {
    private final Object eventContext;
    private final EventMetadata eventMetadata;

    public EventRecord(Object eventContext, EventMetadata eventMetadata) {
        this.eventContext = eventContext;
        this.eventMetadata = eventMetadata;
    }

    public Object getEventContext() {
        return eventContext;
    }

    public EventMetadata getEventMetadata() {
        return eventMetadata;
    }
}
