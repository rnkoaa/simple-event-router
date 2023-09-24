package com.tgt.warehouse;

import java.time.Instant;
import java.util.UUID;

public record EventMetadata(
        UUID eventId,
        Class<?> objectType,
        UUID correlationId,
        Instant processedAt
) {
}
