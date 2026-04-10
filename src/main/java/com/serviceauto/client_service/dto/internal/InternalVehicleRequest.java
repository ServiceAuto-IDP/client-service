package com.serviceauto.client_service.dto.internal;

public record InternalVehicleRequest(
        Long userId,
        String plateNumber,
        String brand,
        String model
) {
}
