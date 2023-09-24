package com.tgt.warehouse;

import com.tgt.warehouse.resilience.Result;

import java.util.Optional;
import java.util.function.Function;

public interface EventProcessorGroup {

    <T> void register(
            Class<T> eventClass,
            Class<?> eventProcessorClass
    );

    <T> void register(
            Class<T> eventClass,
            Class<?> eventProcessorClass,
            Function<Object, EventMetadata> metadataFunction
    );

    Optional<Function<Object, EventMetadata>> findMetadataExtractor(Class<?> clzz);

    Result<?> process(EventRecord eventRecord);
}
