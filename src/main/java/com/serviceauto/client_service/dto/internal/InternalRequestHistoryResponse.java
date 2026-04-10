package com.serviceauto.client_service.dto.internal;

import java.time.Instant;

public record InternalRequestHistoryResponse(
        Long id,
        Long requestId,
        String oldStatus,
        String newStatus,
        Instant changedAt
) {
}
