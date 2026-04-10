package com.serviceauto.client_service.dto;

import java.time.Instant;

public record RequestHistoryResponse(
        Long id,
        Long requestId,
        String oldStatus,
        String newStatus,
        Instant changedAt
) {
}
