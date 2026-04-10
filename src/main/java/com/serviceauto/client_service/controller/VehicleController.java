package com.serviceauto.client_service.controller;

import com.serviceauto.client_service.dto.CreateVehicleRequest;
import com.serviceauto.client_service.dto.UpdateVehicleRequest;
import com.serviceauto.client_service.dto.VehicleResponse;
import com.serviceauto.client_service.security.AuthenticatedUser;
import com.serviceauto.client_service.service.VehicleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse createVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CreateVehicleRequest request
    ) {
        return vehicleService.createVehicle(authenticatedUser.userId(), request);
    }

    @GetMapping
    public List<VehicleResponse> getVehicles(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return vehicleService.getVehiclesForUser(authenticatedUser.userId());
    }

    @GetMapping("/{id}")
    public VehicleResponse getVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        return vehicleService.getVehicle(authenticatedUser.userId(), id);
    }

    @PutMapping("/{id}")
    public VehicleResponse updateVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {
        return vehicleService.updateVehicle(authenticatedUser.userId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ) {
        vehicleService.deleteVehicle(authenticatedUser.userId(), id);
    }
}
