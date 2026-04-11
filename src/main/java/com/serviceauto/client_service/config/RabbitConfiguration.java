package com.serviceauto.client_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    MessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    DirectExchange requestCreatedExchange(ClientServiceProperties properties) {
        return new DirectExchange(properties.messaging().requestCreatedExchange(), true, false);
    }

    @Bean
    Queue requestCreatedQueue(ClientServiceProperties properties) {
        return new Queue(properties.messaging().requestCreatedQueue(), true);
    }

    @Bean
    Binding requestCreatedBinding(
            Queue requestCreatedQueue,
            DirectExchange requestCreatedExchange,
            ClientServiceProperties properties
    ) {
        return BindingBuilder.bind(requestCreatedQueue)
                .to(requestCreatedExchange)
                .with(properties.messaging().requestCreatedRoutingKey());
    }
}


