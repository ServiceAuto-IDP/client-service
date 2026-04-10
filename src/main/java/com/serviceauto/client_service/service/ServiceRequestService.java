package com.serviceauto.client_service.service;

import com.serviceauto.client_service.client.IoServiceClient;
import com.serviceauto.client_service.dto.CreateServiceRequestRequest;
import com.serviceauto.client_service.dto.RequestHistoryResponse;
import com.serviceauto.client_service.dto.ServiceRequestResponse;
import com.serviceauto.client_service.dto.internal.InternalCreateServiceRequest;
import com.serviceauto.client_service.dto.internal.InternalRequestHistoryResponse;
import com.serviceauto.client_service.dto.internal.InternalServiceRequestResponse;
import com.serviceauto.client_service.dto.internal.InternalVehicleResponse;
import com.serviceauto.client_service.exception.UnauthorizedVehicleAccessException;
import com.serviceauto.client_service.messaging.ServiceRequestCreatedMessage;
import com.serviceauto.client_service.messaging.ServiceRequestPublisher;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

    private final IoServiceClient ioServiceClient;
    private final ServiceRequestPublisher serviceRequestPublisher;
    private final MetricsService metricsService;

    public ServiceRequestResponse createRequest(Long userId, CreateServiceRequestRequest request) {
        InternalVehicleResponse vehicle = ioServiceClient.getVehicle(request.vehicleId());
        validateVehicleOwnership(userId, vehicle);

        InternalServiceRequestResponse createdRequest = ioServiceClient.createRequest(new InternalCreateServiceRequest(
                userId,
                request.vehicleId(),
                request.category(),
                request.description(),
                "new",
                Instant.now()
        ));

        serviceRequestPublisher.publish(new ServiceRequestCreatedMessage(
                createdRequest.id(),
                createdRequest.userId(),
                createdRequest.vehicleId(),
                createdRequest.category(),
                createdRequest.createdAt()
        ));

        metricsService.incrementRequestsCreated();
        return toResponse(createdRequest);
    }

    public List<ServiceRequestResponse> getRequestsForUser(Long userId) {
        return ioServiceClient.getRequestsForUser(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceRequestResponse getRequest(Long userId, Long requestId) {
        InternalServiceRequestResponse request = ioServiceClient.getRequest(requestId);
        validateRequestOwnership(userId, request);
        return toResponse(request);
    }

    public List<RequestHistoryResponse> getRequestHistory(Long userId, Long requestId) {
        InternalServiceRequestResponse request = ioServiceClient.getRequest(requestId);
        validateRequestOwnership(userId, request);
        return ioServiceClient.getRequestHistory(requestId).stream()
                .map(this::toResponse)
                .toList();
    }

    private void validateVehicleOwnership(Long userId, InternalVehicleResponse vehicle) {
        if (!userId.equals(vehicle.userId())) {
            throw new UnauthorizedVehicleAccessException(vehicle.id());
        }
    }

    private void validateRequestOwnership(Long userId, InternalServiceRequestResponse request) {
        if (!userId.equals(request.userId())) {
            throw new UnauthorizedVehicleAccessException(
                    "Request does not belong to the authenticated user: " + request.id()
            );
        }
    }

    private ServiceRequestResponse toResponse(InternalServiceRequestResponse request) {
        return new ServiceRequestResponse(
                request.id(),
                request.userId(),
                request.vehicleId(),
                request.category(),
                request.description(),
                request.status(),
                request.estimatedResolutionTime(),
                request.createdAt(),
                request.updatedAt()
        );
    }

    private RequestHistoryResponse toResponse(InternalRequestHistoryResponse history) {
        return new RequestHistoryResponse(
                history.id(),
                history.requestId(),
                history.oldStatus(),
                history.newStatus(),
                history.changedAt()
        );
    }
}
