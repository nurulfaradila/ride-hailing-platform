package com.grab.driver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "driver_locations")
public class DriverLocation {
    @Id
    private String driverId;

    @GeoPointField
    private GeoPoint location;

    private String status;

    public DriverLocation() {
    }

    public DriverLocation(String driverId, GeoPoint location, String status) {
        this.driverId = driverId;
        this.location = location;
        this.status = status;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String driverId;
        private GeoPoint location;
        private String status;

        public Builder driverId(String driverId) {
            this.driverId = driverId;
            return this;
        }

        public Builder location(GeoPoint location) {
            this.location = location;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public DriverLocation build() {
            return new DriverLocation(driverId, location, status);
        }
    }
}
