package com.serviceauto.client_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateVehicleRequest(
        @NotBlank String plateNumber,
        @NotBlank String brand,
        @NotBlank String model
) {
}
