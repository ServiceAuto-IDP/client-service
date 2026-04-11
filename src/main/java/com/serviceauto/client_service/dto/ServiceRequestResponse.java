package com.serviceauto.client_service.dto;

import com.serviceauto.client_service.model.RequestCategory;
import java.time.Instant;

public record ServiceRequestResponse(
        Long id,
        Long userId,
        Long vehicleId,
        RequestCategory category,
        String description,
        String status,
        Instant estimatedResolutionTime,
        Instant createdAt,
        Instant updatedAt
) {
}
