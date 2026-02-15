package com.grab.common.event;

import java.time.LocalDateTime;

public class TripRequestedEvent {
    private String tripId;
    private String riderId;
    private Double pickupLat;
    private Double pickupLng;
    private Double dropoffLat;
    private Double dropoffLng;
    private LocalDateTime requestedAt;

    public TripRequestedEvent() {
    }

    public TripRequestedEvent(String tripId, String riderId, Double pickupLat, Double pickupLng, Double dropoffLat,
            Double dropoffLng, LocalDateTime requestedAt) {
        this.tripId = tripId;
        this.riderId = riderId;
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
        this.requestedAt = requestedAt;
    }

    public String getTripId() {
        return tripId;
    }

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

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tripId;
        private String riderId;
        private Double pickupLat;
        private Double pickupLng;
        private Double dropoffLat;
        private Double dropoffLng;
        private LocalDateTime requestedAt;

        public Builder tripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public Builder riderId(String riderId) {
            this.riderId = riderId;
            return this;
        }

        public Builder pickupLat(Double pickupLat) {
            this.pickupLat = pickupLat;
            return this;
        }

        public Builder pickupLng(Double pickupLng) {
            this.pickupLng = pickupLng;
            return this;
        }

        public Builder dropoffLat(Double dropoffLat) {
            this.dropoffLat = dropoffLat;
            return this;
        }

        public Builder dropoffLng(Double dropoffLng) {
            this.dropoffLng = dropoffLng;
            return this;
        }

        public Builder requestedAt(LocalDateTime requestedAt) {
            this.requestedAt = requestedAt;
            return this;
        }

        public TripRequestedEvent build() {
            return new TripRequestedEvent(tripId, riderId, pickupLat, pickupLng, dropoffLat, dropoffLng, requestedAt);
        }
    }
}
