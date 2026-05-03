package com.serviceauto.client_service.dto;

public record VehicleResponse(
        Long id,
        Long userId,
        String licensePlate,
        String brand,
        String model
) {
}
