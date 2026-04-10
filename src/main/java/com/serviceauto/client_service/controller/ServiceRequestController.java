package com.serviceauto.client_service.controller;

import com.serviceauto.client_service.dto.CreateServiceRequestRequest;
import com.serviceauto.client_service.dto.RequestHistoryResponse;
import com.serviceauto.client_service.dto.ServiceRequestResponse;
import com.serviceauto.client_service.security.AuthenticatedUser;
import com.serviceauto.client_service.service.ServiceRequestService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceRequestResponse createRequest(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CreateServiceRequestRequest request
    ) {
        return serviceRequestService.createRequest(authenticatedUser.userId(), request);
    }

    @GetMapping
    public List<ServiceRequestResponse> listRequests(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return serviceRequestService.getRequestsForUser(authenticatedUser.userId());
    }

    @GetMapping("/{id}")
    public ServiceRequestResponse getRequest(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        return serviceRequestService.getRequest(authenticatedUser.userId(), id);
    }

    @GetMapping("/{id}/history")
    public List<RequestHistoryResponse> getRequestHistory(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        return serviceRequestService.getRequestHistory(authenticatedUser.userId(), id);
    }
}
