package com.serviceauto.client_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateVehicleRequest(
        @NotBlank String licensePlate,
        @NotBlank String brand,
        @NotBlank String model
) {
}
