package com.grab.dispatch.service;

import com.grab.common.event.TripAssignedEvent;
import com.grab.common.event.TripRequestedEvent;
import com.grab.dispatch.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DispatchService {

    private static final Logger log = LoggerFactory.getLogger(DispatchService.class);

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    public DispatchService(RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    @Value("${services.driver.url}")
    private String driverServiceUrl;

    @RabbitListener(queues = RabbitMQConfig.TRIP_REQUEST_QUEUE)
    public void handleTripRequest(TripRequestedEvent event) {
        log.info("Received trip request: {}", event.getTripId());

        String url = String.format("%s/api/drivers/nearby?lat=%s&lng=%s&radius=5.0",
                driverServiceUrl, event.getPickupLat(), event.getPickupLng());

        try {
            ResponseEntity<List<String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {
                    });

            List<String> nearbyDriverIds = response.getBody();

            if (nearbyDriverIds != null && !nearbyDriverIds.isEmpty()) {
                int randomIndex = (int) (Math.random() * nearbyDriverIds.size());
                String selectedDriverId = nearbyDriverIds.get(randomIndex);
                log.info("Found {} nearby drivers. Randomly assigning driver {} to trip {}",
                        nearbyDriverIds.size(), selectedDriverId, event.getTripId());

                TripAssignedEvent assignEvent = TripAssignedEvent.builder()
                        .tripId(event.getTripId())
                        .driverId(selectedDriverId)
                        .riderId(event.getRiderId())
                        .build();

                rabbitTemplate.convertAndSend(RabbitMQConfig.TRIP_EXCHANGE, RabbitMQConfig.TRIP_ASSIGN_ROUTING_KEY,
                        assignEvent);
            } else {
                log.warn("No drivers found nearby for trip {}", event.getTripId());
            }
        } catch (Exception e) {
            log.error("Error finding drivers for trip {}: {}", event.getTripId(), e.getMessage());
        }
    }
}
