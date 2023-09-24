package com.tgt.warehouse;

import com.tgt.warehouse.exceptions.NotYetImplementedException;
import com.tgt.warehouse.resilience.Result;

public interface EventProcessor<T> {

    default Result<?> process(T event){
        return Result.failure(new NotYetImplementedException());
    }
    default Result<?> process(EventMetadata eventMetadata, T event){
        return Result.failure(new NotYetImplementedException());
    }

    Class<T> getType();
}
