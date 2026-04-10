package com.serviceauto.client_service.dto;

public record VehicleResponse(
        Long id,
        Long userId,
        String plateNumber,
        String brand,
        String model
) {
}
