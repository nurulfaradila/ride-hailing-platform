package com.grab.driver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    private String id;
    private String name;
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    public enum DriverStatus {
        AVAILABLE, BUSY, OFFLINE
    }

    public Driver() {
    }

    public Driver(String id, String name, String licensePlate, DriverStatus status) {
        this.id = id;
        this.name = name;
        this.licensePlate = licensePlate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String licensePlate;
        private DriverStatus status;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder licensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
            return this;
        }

        public Builder status(DriverStatus status) {
            this.status = status;
            return this;
        }

        public Driver build() {
            return new Driver(id, name, licensePlate, status);
        }
    }
}
