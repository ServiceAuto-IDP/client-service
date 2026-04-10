package com.serviceauto.client_service.exception;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(Long vehicleId) {
        super("Vehicle not found: " + vehicleId);
    }
}
