package com.tgt.warehouse;

import com.tgt.warehouse.exceptions.InvalidArgumentException;
import com.tgt.warehouse.resilience.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class EventProcessorGroupImpl implements EventProcessorGroup {

    Map<Class<?>, Class<?>> eventHandlerClass = new HashMap<>();

    // concrete implementations of event handlers that have been registered priori
    Map<Class<?>, Object> eventHandlers = new HashMap<>();

    Map<Class<?>, Function<Object, EventMetadata>> eventMetadataFunctions = new HashMap<>();

    @Override
    public <T> void register(Class<T> eventClass, Class<?> eventProcessorClass) {
        eventHandlerClass.put(eventClass, eventProcessorClass);
    }

    @Override
    public <T> void register(Class<T> eventClass, Class<?> eventProcessorClass, Function<Object, EventMetadata> metadataFunction) {
        register(eventClass, eventProcessorClass);
        eventMetadataFunctions.put(eventClass, metadataFunction);
    }

    @Override
    public Optional<Function<Object, EventMetadata>> findMetadataExtractor(Class<?> clzz) {
        return Optional.ofNullable(eventMetadataFunctions.get(clzz));
    }

    @Override
    public Result<?> process(EventRecord eventRecord) {
        var eventContext = eventRecord.getEventContext();
        if (eventContext == null) {
            return Result.failure(new InvalidArgumentException("event object is null"));
        }
        var eventContextClass = eventContext.getClass();
        var handlerClass = eventHandlerClass.get(eventContextClass);
        if (handlerClass == null) {
            return Result.failure(
                    new InvalidArgumentException("No event handler class registered for this event " + eventContextClass)
            );
        }

        return handleEvent(eventContext, handlerClass);
    }

    @SuppressWarnings("unchecked")
    private Result<?> handleEvent(Object eventContext, Class<?> handlerClass) {
        var handler = eventHandlers.get(handlerClass);
        if (handler == null) {
            return Result.failure(new InvalidArgumentException(
                    "no handler object found to handle event " + eventContext.getClass().getName()
            ));
        }

        @SuppressWarnings("rawtypes")
        var eventProcessor = (EventProcessor) handler;

        Class<?> eventType = eventProcessor.getType();

        return eventProcessor.process(eventType.cast(eventContext));
    }
}
