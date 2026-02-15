package com.grab.common.event;

public class TripAssignedEvent {
    private String tripId;
    private String driverId;
    private String riderId;

    public TripAssignedEvent() {
    }

    public TripAssignedEvent(String tripId, String driverId, String riderId) {
        this.tripId = tripId;
        this.driverId = driverId;
        this.riderId = riderId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tripId;
        private String driverId;
        private String riderId;

        public Builder tripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public Builder driverId(String driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder riderId(String riderId) {
            this.riderId = riderId;
            return this;
        }

        public TripAssignedEvent build() {
            return new TripAssignedEvent(tripId, driverId, riderId);
        }
    }
}
