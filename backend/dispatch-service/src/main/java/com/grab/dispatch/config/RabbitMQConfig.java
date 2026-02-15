package com.grab.dispatch.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TRIP_EXCHANGE = "trip.exchange";
    public static final String TRIP_REQUEST_QUEUE = "trip.request.queue";
    public static final String TRIP_ASSIGN_ROUTING_KEY = "trip.assign";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
