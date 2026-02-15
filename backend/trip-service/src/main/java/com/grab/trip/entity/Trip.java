package com.grab.trip.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    private String id;
    private String riderId;
    private String driverId;

    private Double pickupLat;
    private Double pickupLng;
    private Double dropoffLat;
    private Double dropoffLng;

    private Double distance; // in km
    private Double estimatedPrice;
    private Double estimatedTime; // in minutes

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum TripStatus {
        REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public Trip() {
    }

    public Trip(String id, String riderId, String driverId, Double pickupLat, Double pickupLng, Double dropoffLat,
            Double dropoffLng, TripStatus status, Double distance, Double estimatedPrice, Double estimatedTime) {
        this.id = id;
        this.riderId = riderId;
        this.driverId = driverId;
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
        this.status = status;
        this.distance = distance;
        this.estimatedPrice = estimatedPrice;
        this.estimatedTime = estimatedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(Double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public Double getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(Double pickupLng) {
        this.pickupLng = pickupLng;
    }

    public Double getDropoffLat() {
        return dropoffLat;
    }

    public void setDropoffLat(Double dropoffLat) {
        this.dropoffLat = dropoffLat;
    }

    public Double getDropoffLng() {
        return dropoffLng;
    }

    public void setDropoffLng(Double dropoffLng) {
        this.dropoffLng = dropoffLng;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String riderId;
        private String driverId;
        private Double pickupLat;
        private Double pickupLng;
        private Double dropoffLat;
        private Double dropoffLng;
        private TripStatus status;
        private Double distance;
        private Double estimatedPrice;
        private Double estimatedTime;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder riderId(String riderId) {
            this.riderId = riderId;
            return this;
        }

        public Builder driverId(String driverId) {
            this.driverId = driverId;
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

        public Builder status(TripStatus status) {
            this.status = status;
            return this;
        }

        public Builder distance(Double distance) {
            this.distance = distance;
            return this;
        }

        public Builder estimatedPrice(Double estimatedPrice) {
            this.estimatedPrice = estimatedPrice;
            return this;
        }

        public Builder estimatedTime(Double estimatedTime) {
            this.estimatedTime = estimatedTime;
            return this;
        }

        public Trip build() {
            return new Trip(id, riderId, driverId, pickupLat, pickupLng, dropoffLat, dropoffLng, status, distance,
                    estimatedPrice, estimatedTime);
        }
    }
}
