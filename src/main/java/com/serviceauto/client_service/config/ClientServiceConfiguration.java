package com.serviceauto.client_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(ClientServiceProperties.class)
public class ClientServiceConfiguration {

    @Bean
    RestClient ioServiceRestClient(RestClient.Builder builder, ClientServiceProperties properties) {
        return builder
                .baseUrl(properties.ioService().baseUrl())
                .build();
    }
}
