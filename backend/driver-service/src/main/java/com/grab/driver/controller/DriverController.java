package com.grab.driver.controller;

import com.grab.common.dto.LocationDTO;
import com.grab.driver.entity.Driver;
import com.grab.driver.service.DriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PatchMapping("/{id}/location")
    public void updateLocation(@PathVariable String id, @RequestBody LocationDTO location) {
        driverService.updateLocation(id, location);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable String id, @RequestParam Driver.DriverStatus status) {
        driverService.updateStatus(id, status);
    }

    @GetMapping("/nearby")
    public List<String> findNearbyDrivers(@RequestParam Double lat, @RequestParam Double lng,
            @RequestParam Double radius) {
        return driverService.findNearbyDrivers(lat, lng, radius);
    }

    @GetMapping("/{id}")
    public Driver getDriver(@PathVariable String id) {
        return driverService.getDriver(id);
    }

    @DeleteMapping("/purge")
    public void purgeAllDrivers() {
        driverService.purgeAllDrivers();
    }
}
