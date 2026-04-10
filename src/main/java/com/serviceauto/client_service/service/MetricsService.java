package com.serviceauto.client_service.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter createdVehiclesCounter;
    private final Counter createdRequestsCounter;

    public MetricsService(MeterRegistry meterRegistry) {
        this.createdVehiclesCounter = Counter.builder("client_service_vehicles_created_total")
                .description("Number of vehicles created")
                .register(meterRegistry);
        this.createdRequestsCounter = Counter.builder("client_service_requests_created_total")
                .description("Number of service requests created")
                .register(meterRegistry);
    }

    public void incrementVehiclesCreated() {
        createdVehiclesCounter.increment();
    }

    public void incrementRequestsCreated() {
        createdRequestsCounter.increment();
    }
}
