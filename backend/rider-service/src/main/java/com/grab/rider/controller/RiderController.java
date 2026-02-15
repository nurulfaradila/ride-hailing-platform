package com.grab.rider.controller;

import com.grab.rider.entity.Rider;
import com.grab.rider.repository.RiderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/riders")
@CrossOrigin
public class RiderController {

    private final RiderRepository riderRepository;

    public RiderController(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @PostMapping
    public Rider createRider(@RequestBody Rider rider) {
        return riderRepository.save(rider);
    }

    @GetMapping("/{id}")
    public Rider getRider(@PathVariable String id) {
        return riderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rider not found"));
    }

    @GetMapping
    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }
}
