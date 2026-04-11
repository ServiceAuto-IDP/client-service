package com.serviceauto.client_service.client;

import com.serviceauto.client_service.dto.internal.InternalCreateServiceRequest;
import com.serviceauto.client_service.dto.internal.InternalRequestHistoryResponse;
import com.serviceauto.client_service.dto.internal.InternalServiceRequestResponse;
import com.serviceauto.client_service.dto.internal.InternalVehicleRequest;
import com.serviceauto.client_service.dto.internal.InternalVehicleResponse;
import com.serviceauto.client_service.exception.InvalidIoRequestException;
import com.serviceauto.client_service.exception.RequestNotFoundException;
import com.serviceauto.client_service.exception.UpstreamServiceException;
import com.serviceauto.client_service.exception.VehicleNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class IoServiceClient {

    private final RestClient ioServiceRestClient;

    public InternalVehicleResponse createVehicle(InternalVehicleRequest request) {
        return execute(() -> ioServiceRestClient.post()
                .uri("/internal/vehicles")
                .body(request)
                .retrieve()
                .body(InternalVehicleResponse.class), "Failed to create vehicle in io-service");
    }

    public List<InternalVehicleResponse> getVehiclesForUser(Long userId) {
        InternalVehicleResponse[] response = execute(() -> ioServiceRestClient.get()
                .uri("/internal/users/{userId}/vehicles", userId)
                .retrieve()
                .body(InternalVehicleResponse[].class), "Failed to fetch vehicles from io-service");
        return response == null ? List.of() : List.of(response);
    }

    public InternalVehicleResponse getVehicle(Long vehicleId) {
        try {
            return ioServiceRestClient.get()
                    .uri("/internal/vehicles/{vehicleId}", vehicleId)
                    .retrieve()
                    .body(InternalVehicleResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new VehicleNotFoundException(vehicleId);
        } catch (RestClientException exception) {
            throw new UpstreamServiceException("Failed to fetch vehicle from io-service");
        }
    }

    public InternalVehicleResponse updateVehicle(Long vehicleId, InternalVehicleRequest request) {
        try {
            return ioServiceRestClient.put()
                    .uri("/internal/vehicles/{vehicleId}", vehicleId)
                    .body(request)
                    .retrieve()
                    .body(InternalVehicleResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new VehicleNotFoundException(vehicleId);
        } catch (RestClientException exception) {
            throw new UpstreamServiceException("Failed to update vehicle in io-service");
        }
    }

    public void deleteVehicle(Long vehicleId) {
        try {
            ioServiceRestClient.delete()
                    .uri("/internal/vehicles/{vehicleId}", vehicleId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new VehicleNotFoundException(vehicleId);
        } catch (RestClientException exception) {
            throw new UpstreamServiceException("Failed to delete vehicle in io-service");
        }
    }

    public InternalServiceRequestResponse createRequest(InternalCreateServiceRequest request) {
        return execute(() -> ioServiceRestClient.post()
                .uri("/internal/requests")
                .body(request)
                .retrieve()
                .body(InternalServiceRequestResponse.class), "Failed to create service request in io-service");
    }

    public List<InternalServiceRequestResponse> getRequestsForUser(Long userId) {
        InternalServiceRequestResponse[] response = execute(() -> ioServiceRestClient.get()
                .uri("/internal/users/{userId}/requests", userId)
                .retrieve()
                .body(InternalServiceRequestResponse[].class), "Failed to fetch service requests from io-service");
        return response == null ? List.of() : List.of(response);
    }

    public InternalServiceRequestResponse getRequest(Long requestId) {
        try {
            return ioServiceRestClient.get()
                    .uri("/internal/requests/{requestId}", requestId)
                    .retrieve()
                    .body(InternalServiceRequestResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new RequestNotFoundException(requestId);
        } catch (RestClientException exception) {
            throw new UpstreamServiceException("Failed to fetch service request from io-service");
        }
    }

    public List<InternalRequestHistoryResponse> getRequestHistory(Long requestId) {
        try {
            InternalRequestHistoryResponse[] response = ioServiceRestClient.get()
                    .uri("/internal/requests/{requestId}/history", requestId)
                    .retrieve()
                    .body(InternalRequestHistoryResponse[].class);
            return response == null ? List.of() : List.of(response);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new RequestNotFoundException(requestId);
        } catch (RestClientException exception) {
            throw new UpstreamServiceException("Failed to fetch request history from io-service");
        }
    }

    private <T> T execute(IoCall<T> call, String errorMessage) {
        try {
            return call.call();
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new InvalidIoRequestException(extractMessage(exception));
        } catch (RestClientException exception) {
            throw new UpstreamServiceException(errorMessage);
        }
    }

    private String extractMessage(HttpClientErrorException exception) {
        String responseBody = exception.getResponseBodyAsString();
        return responseBody == null || responseBody.isBlank()
                ? "Invalid request sent to io-service"
                : responseBody;
    }

    @FunctionalInterface
    private interface IoCall<T> {
        T call();
    }
}
