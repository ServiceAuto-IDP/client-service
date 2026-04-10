package com.serviceauto.client_service.service;

import com.serviceauto.client_service.client.IoServiceClient;
import com.serviceauto.client_service.dto.CreateVehicleRequest;
import com.serviceauto.client_service.dto.UpdateVehicleRequest;
import com.serviceauto.client_service.dto.VehicleResponse;
import com.serviceauto.client_service.dto.internal.InternalVehicleRequest;
import com.serviceauto.client_service.dto.internal.InternalVehicleResponse;
import com.serviceauto.client_service.exception.UnauthorizedVehicleAccessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final IoServiceClient ioServiceClient;
    private final MetricsService metricsService;

    public VehicleResponse createVehicle(Long userId, CreateVehicleRequest request) {
        InternalVehicleResponse vehicle = ioServiceClient.createVehicle(new InternalVehicleRequest(
                userId,
                request.plateNumber(),
                request.brand(),
                request.model()
        ));
        metricsService.incrementVehiclesCreated();
        return toResponse(vehicle);
    }

    public List<VehicleResponse> getVehiclesForUser(Long userId) {
        return ioServiceClient.getVehiclesForUser(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public VehicleResponse getVehicle(Long userId, Long vehicleId) {
        InternalVehicleResponse vehicle = ioServiceClient.getVehicle(vehicleId);
        validateVehicleOwnership(userId, vehicle);
        return toResponse(vehicle);
    }

    public VehicleResponse updateVehicle(Long userId, Long vehicleId, UpdateVehicleRequest request) {
        InternalVehicleResponse existingVehicle = ioServiceClient.getVehicle(vehicleId);
        validateVehicleOwnership(userId, existingVehicle);
        InternalVehicleResponse updatedVehicle = ioServiceClient.updateVehicle(vehicleId, new InternalVehicleRequest(
                userId,
                request.plateNumber(),
                request.brand(),
                request.model()
        ));
        return toResponse(updatedVehicle);
    }

    public void deleteVehicle(Long userId, Long vehicleId) {
        InternalVehicleResponse existingVehicle = ioServiceClient.getVehicle(vehicleId);
        validateVehicleOwnership(userId, existingVehicle);
        ioServiceClient.deleteVehicle(vehicleId);
    }

    private void validateVehicleOwnership(Long userId, InternalVehicleResponse vehicle) {
        if (!userId.equals(vehicle.userId())) {
            throw new UnauthorizedVehicleAccessException(vehicle.id());
        }
    }

    private VehicleResponse toResponse(InternalVehicleResponse vehicle) {
        return new VehicleResponse(
                vehicle.id(),
                vehicle.userId(),
                vehicle.plateNumber(),
                vehicle.brand(),
                vehicle.model()
        );
    }
}
