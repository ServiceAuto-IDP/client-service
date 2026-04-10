package com.serviceauto.client_service.messaging;

import com.serviceauto.client_service.model.RequestCategory;
import java.time.Instant;

public record ServiceRequestCreatedMessage(
        Long requestId,
        Long userId,
        Long vehicleId,
        RequestCategory category,
        Instant createdAt
) {
}
