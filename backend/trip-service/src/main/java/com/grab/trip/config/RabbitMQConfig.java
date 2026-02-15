package com.grab.trip.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TRIP_EXCHANGE = "trip.exchange";
    public static final String TRIP_REQUEST_QUEUE = "trip.request.queue";
    public static final String TRIP_ASSIGN_QUEUE = "trip.assign.queue";
    public static final String TRIP_REQUEST_ROUTING_KEY = "trip.request";
    public static final String TRIP_ASSIGN_ROUTING_KEY = "trip.assign";

    @Bean
    public DirectExchange tripExchange() {
        return new DirectExchange(TRIP_EXCHANGE);
    }

    @Bean
    public Queue tripRequestQueue() {
        return new Queue(TRIP_REQUEST_QUEUE);
    }

    @Bean
    public Binding tripRequestBinding(Queue tripRequestQueue, DirectExchange tripExchange) {
        return BindingBuilder.bind(tripRequestQueue).to(tripExchange).with(TRIP_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue tripAssignQueue() {
        return new Queue(TRIP_ASSIGN_QUEUE);
    }

    @Bean
    public Binding tripAssignBinding(Queue tripAssignQueue, DirectExchange tripExchange) {
        return BindingBuilder.bind(tripAssignQueue).to(tripExchange).with(TRIP_ASSIGN_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
