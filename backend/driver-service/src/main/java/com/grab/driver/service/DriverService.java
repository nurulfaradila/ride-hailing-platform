package com.grab.driver.service;

import com.grab.common.dto.LocationDTO;
import com.grab.driver.entity.Driver;
import com.grab.driver.model.DriverLocation;
import com.grab.driver.repository.DriverRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ElasticsearchOperations elasticsearchOperations;

    public DriverService(DriverRepository driverRepository,
            RedisTemplate<String, Object> redisTemplate,
            ElasticsearchOperations elasticsearchOperations) {
        this.driverRepository = driverRepository;
        this.redisTemplate = redisTemplate;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    private static final String DRIVER_LOCATION_KEY_PREFIX = "driver:location:";

    @Transactional
    public void updateLocation(String driverId, LocationDTO location) {
        redisTemplate.opsForValue().set(DRIVER_LOCATION_KEY_PREFIX + driverId, location);

        Driver driver = driverRepository.findById(driverId)
                .orElseGet(() -> {
                    Driver newDriver = new Driver(driverId, "Simulation Driver " + driverId, "PRO-123",
                            Driver.DriverStatus.AVAILABLE);
                    return driverRepository.save(newDriver);
                });

        DriverLocation driverLocation = DriverLocation.builder()
                .driverId(driverId)
                .location(new GeoPoint(location.getLat(), location.getLng()))
                .status(driver.getStatus().name())
                .build();
        elasticsearchOperations.save(driverLocation);
    }

    public List<String> findNearbyDrivers(Double lat, Double lng, Double radiusInKm) {
        Criteria criteria = new Criteria("location")
                .within(new GeoPoint(lat, lng), radiusInKm + "km")
                .and(new Criteria("status").is("AVAILABLE"));

        Query query = new CriteriaQuery(criteria);

        SearchHits<DriverLocation> searchHits = elasticsearchOperations.search(query, DriverLocation.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(DriverLocation::getDriverId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStatus(String driverId, Driver.DriverStatus status) {
        Driver driver = driverRepository.findById(driverId)
                .orElseGet(() -> {
                    Driver newDriver = new Driver(driverId, "Simulation Driver " + driverId, "PRO-123", status);
                    return driverRepository.save(newDriver);
                });

        driver.setStatus(status);
        driverRepository.save(driver);

        LocationDTO lastLocation = (LocationDTO) redisTemplate.opsForValue().get(DRIVER_LOCATION_KEY_PREFIX + driverId);
        if (lastLocation != null) {
            DriverLocation driverLocation = DriverLocation.builder()
                    .driverId(driverId)
                    .location(new GeoPoint(lastLocation.getLat(), lastLocation.getLng()))
                    .status(status.name())
                    .build();
            elasticsearchOperations.save(driverLocation);
        }
    }

    public Driver getDriver(String driverId) {
        return driverRepository.findById(driverId)
                .orElseGet(() -> {
                    Driver newDriver = new Driver(driverId, "Simulation Driver " + driverId, "PRO-123",
                            Driver.DriverStatus.AVAILABLE);
                    return driverRepository.save(newDriver);
                });
    }

    @Transactional
    public void purgeAllDrivers() {
        driverRepository.deleteAll();
        elasticsearchOperations.indexOps(DriverLocation.class).delete();
        elasticsearchOperations.indexOps(DriverLocation.class).create();
        elasticsearchOperations.indexOps(DriverLocation.class).putMapping();

        java.util.Set<String> keys = redisTemplate.keys(DRIVER_LOCATION_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
