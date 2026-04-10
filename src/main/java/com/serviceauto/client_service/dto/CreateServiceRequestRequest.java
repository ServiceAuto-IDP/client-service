package com.serviceauto.client_service.dto;

import com.serviceauto.client_service.model.RequestCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateServiceRequestRequest(
        @NotNull Long vehicleId,
        @NotNull RequestCategory category,
        @NotBlank String description
) {
}
