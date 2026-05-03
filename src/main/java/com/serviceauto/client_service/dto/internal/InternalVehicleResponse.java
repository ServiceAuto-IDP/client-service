package com.serviceauto.client_service.dto.internal;

public record InternalVehicleResponse(
        Long id,
        Long userId,
        String licensePlate,
        String brand,
        String model
) {
}
