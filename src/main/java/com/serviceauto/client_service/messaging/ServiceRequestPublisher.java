package com.serviceauto.client_service.messaging;

import com.serviceauto.client_service.config.ClientServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceRequestPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ClientServiceProperties properties;

    public void publish(ServiceRequestCreatedMessage message) {
        rabbitTemplate.convertAndSend(
                properties.messaging().requestCreatedExchange(),
                properties.messaging().requestCreatedRoutingKey(),
                message
        );
    }
}
