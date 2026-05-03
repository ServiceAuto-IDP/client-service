package com.serviceauto.client_service.dto.internal;

public record InternalVehicleRequest(
        Long userId,
        String licensePlate,
        String brand,
        String model
) {
}
