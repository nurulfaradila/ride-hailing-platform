package com.grab.trip.service;

import com.grab.common.event.TripAssignedEvent;
import com.grab.common.event.TripRequestedEvent;
import com.grab.trip.config.RabbitMQConfig;
import com.grab.trip.entity.Trip;
import com.grab.trip.repository.TripRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final RabbitTemplate rabbitTemplate;

    public TripService(TripRepository tripRepository, RabbitTemplate rabbitTemplate) {
        this.tripRepository = tripRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Trip requestTrip(String riderId, Double pickupLat, Double pickupLng, Double dropoffLat, Double dropoffLng) {
        String tripId = UUID.randomUUID().toString();

        Double distance = calculateDistance(pickupLat, pickupLng, dropoffLat, dropoffLng);
        Double estimatedPrice = calculatePrice(distance);
        Double estimatedTime = calculateEstimatedTime(distance);

        Trip trip = Trip.builder()
                .id(tripId)
                .riderId(riderId)
                .pickupLat(pickupLat)
                .pickupLng(pickupLng)
                .dropoffLat(dropoffLat)
                .dropoffLng(dropoffLng)
                .status(Trip.TripStatus.REQUESTED)
                .distance(distance)
                .estimatedPrice(estimatedPrice)
                .estimatedTime(estimatedTime)
                .build();

        Trip savedTrip = tripRepository.save(trip);

        TripRequestedEvent event = TripRequestedEvent.builder()
                .tripId(tripId)
                .riderId(riderId)
                .pickupLat(pickupLat)
                .pickupLng(pickupLng)
                .dropoffLat(dropoffLat)
                .dropoffLng(dropoffLng)
                .requestedAt(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.TRIP_EXCHANGE, RabbitMQConfig.TRIP_REQUEST_ROUTING_KEY, event);

        return savedTrip;
    }

    @RabbitListener(queues = RabbitMQConfig.TRIP_ASSIGN_QUEUE)
    @Transactional
    public void handleTripAssigned(TripAssignedEvent event) {
        Trip trip = tripRepository.findById(event.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        trip.setDriverId(event.getDriverId());
        trip.setStatus(Trip.TripStatus.ASSIGNED);
        tripRepository.save(trip);
    }

    public Trip getTrip(String id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    @Transactional
    public void purgeAllTrips() {
        tripRepository.deleteAll();
    }

    @Transactional
    public void completeTrip(String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        trip.setStatus(Trip.TripStatus.COMPLETED);
        tripRepository.save(trip);
    }

    @Transactional
    public void cancelTrip(String tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
        trip.setStatus(Trip.TripStatus.CANCELLED);
        tripRepository.save(trip);
    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // in km
        return Math.round(distance * 100.0) / 100.0;
    }

    private Double calculatePrice(Double distance) {
        double baseFare = 5.0;
        double costPerKm = 2.5;
        return Math.round((baseFare + (distance * costPerKm)) * 100.0) / 100.0;
    }

    private Double calculateEstimatedTime(Double distance) {
        double averageSpeedKmH = 40.0; // Assumed average speed
        double timeInHours = distance / averageSpeedKmH;
        return Math.round((timeInHours * 60) * 100.0) / 100.0; // in minutes
    }
}
