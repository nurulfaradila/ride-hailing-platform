package com.grab.trip.controller;

import com.grab.trip.entity.Trip;
import com.grab.trip.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public Trip requestTrip(@RequestBody TripRequest request) {
        return tripService.requestTrip(
                request.getRiderId(),
                request.getPickupLat(),
                request.getPickupLng(),
                request.getDropoffLat(),
                request.getDropoffLng());
    }

    @GetMapping("/{id}")
    public Trip getTrip(@PathVariable String id) {
        return tripService.getTrip(id);
    }

    @DeleteMapping("/purge")
    public ResponseEntity<?> purgeAllTrips() {
        try {
            tripService.purgeAllTrips();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Purge failed: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeTrip(@PathVariable String id) {
        try {
            tripService.completeTrip(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Complete failed: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelTrip(@PathVariable String id) {
        try {
            tripService.cancelTrip(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Cancel failed: " + e.getMessage());
        }
    }

    public static class TripRequest {
        private String riderId;
        private Double pickupLat;
        private Double pickupLng;
        private Double dropoffLat;
        private Double dropoffLng;

        public String getRiderId() {
            return riderId;
        }

        public Double getPickupLat() {
            return pickupLat;
        }

        public Double getPickupLng() {
            return pickupLng;
        }

        public Double getDropoffLat() {
            return dropoffLat;
        }

        public Double getDropoffLng() {
            return dropoffLng;
        }

        public void setRiderId(String riderId) {
            this.riderId = riderId;
        }

        public void setPickupLat(Double pickupLat) {
            this.pickupLat = pickupLat;
        }

        public void setPickupLng(Double pickupLng) {
            this.pickupLng = pickupLng;
        }

        public void setDropoffLat(Double dropoffLat) {
            this.dropoffLat = dropoffLat;
        }

        public void setDropoffLng(Double dropoffLng) {
            this.dropoffLng = dropoffLng;
        }
    }
}
