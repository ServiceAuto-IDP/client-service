package com.serviceauto.client_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.serviceauto.client_service.client.IoServiceClient;
import com.serviceauto.client_service.dto.CreateVehicleRequest;
import com.serviceauto.client_service.dto.VehicleResponse;
import com.serviceauto.client_service.dto.internal.InternalVehicleResponse;
import com.serviceauto.client_service.exception.UnauthorizedVehicleAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private IoServiceClient ioServiceClient;

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicleReturnsCreatedVehicle() {
        when(ioServiceClient.createVehicle(any())).thenReturn(
                new InternalVehicleResponse(1L, 7L, "B123XYZ", "Volkswagen", "Golf 7")
        );

        VehicleResponse response = vehicleService.createVehicle(
                7L,
                new CreateVehicleRequest("B123XYZ", "Volkswagen", "Golf 7")
        );

        assertEquals(1L, response.id());
        assertEquals("B123XYZ", response.licensePlate());
        verify(metricsService).incrementVehiclesCreated();
    }

    @Test
    void getVehicleRejectsVehicleFromAnotherUser() {
        when(ioServiceClient.getVehicle(1L)).thenReturn(
                new InternalVehicleResponse(1L, 8L, "B123XYZ", "Volkswagen", "Golf 7")
        );

        assertThrows(UnauthorizedVehicleAccessException.class, () -> vehicleService.getVehicle(7L, 1L));
    }
}
