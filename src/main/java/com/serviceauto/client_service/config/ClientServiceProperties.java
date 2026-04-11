package com.serviceauto.client_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ClientServiceProperties(
        IoService ioService,
        Messaging messaging
) {

    public record IoService(String baseUrl) {
    }

    public record Messaging(String requestCreatedExchange, String requestCreatedQueue, String requestCreatedRoutingKey) {
    }
}

