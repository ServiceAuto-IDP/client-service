package com.serviceauto.client_service.exception;

public class UnauthorizedVehicleAccessException extends RuntimeException {

    public UnauthorizedVehicleAccessException(Long vehicleId) {
        super("Vehicle does not belong to the authenticated user: " + vehicleId);
    }

    public UnauthorizedVehicleAccessException(String message) {
        super(message);
    }
}
