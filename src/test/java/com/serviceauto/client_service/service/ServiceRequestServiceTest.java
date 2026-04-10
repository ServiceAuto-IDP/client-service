package com.serviceauto.client_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.serviceauto.client_service.client.IoServiceClient;
import com.serviceauto.client_service.dto.CreateServiceRequestRequest;
import com.serviceauto.client_service.dto.ServiceRequestResponse;
import com.serviceauto.client_service.dto.internal.InternalServiceRequestResponse;
import com.serviceauto.client_service.dto.internal.InternalVehicleResponse;
import com.serviceauto.client_service.exception.UnauthorizedVehicleAccessException;
import com.serviceauto.client_service.messaging.ServiceRequestPublisher;
import com.serviceauto.client_service.model.RequestCategory;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceTest {

    @Mock
    private IoServiceClient ioServiceClient;

    @Mock
    private ServiceRequestPublisher serviceRequestPublisher;

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    @Test
    void createRequestPublishesMessageForOwnedVehicle() {
        when(ioServiceClient.getVehicle(15L)).thenReturn(
                new InternalVehicleResponse(15L, 7L, "B123XYZ", "Volkswagen", "Golf 7")
        );
        when(ioServiceClient.createRequest(any())).thenReturn(new InternalServiceRequestResponse(
                101L,
                7L,
                15L,
                RequestCategory.MECHANICAL,
                "Noise when braking",
                "new",
                null,
                Instant.now(),
                Instant.now()
        ));

        ServiceRequestResponse response = serviceRequestService.createRequest(
                7L,
                new CreateServiceRequestRequest(15L, RequestCategory.MECHANICAL, "Noise when braking")
        );

        assertEquals(101L, response.id());
        assertEquals("new", response.status());
        verify(serviceRequestPublisher).publish(any());
        verify(metricsService).incrementRequestsCreated();
    }

    @Test
    void createRequestRejectsVehicleFromAnotherUser() {
        when(ioServiceClient.getVehicle(15L)).thenReturn(
                new InternalVehicleResponse(15L, 8L, "B123XYZ", "Volkswagen", "Golf 7")
        );

        assertThrows(UnauthorizedVehicleAccessException.class, () -> serviceRequestService.createRequest(
                7L,
                new CreateServiceRequestRequest(15L, RequestCategory.MECHANICAL, "Noise")
        ));
    }
}
