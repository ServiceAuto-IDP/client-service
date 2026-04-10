package com.serviceauto.client_service.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateVehicleRequest(
        @NotBlank String plateNumber,
        @NotBlank String brand,
        @NotBlank String model
) {
}
