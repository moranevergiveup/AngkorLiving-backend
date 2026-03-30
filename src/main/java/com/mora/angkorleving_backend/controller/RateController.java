package com.mora.angkorleving_backend.controller;

import com.mora.angkorleving_backend.config.RateConfig;
import com.mora.angkorleving_backend.DTOs.request.RateUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final RateConfig rateConfig;

    @GetMapping
    public ResponseEntity<RateConfig> getRates() {
        return ResponseEntity.ok(rateConfig);
    }

    @PutMapping
    public ResponseEntity<RateConfig> updateRates(@RequestBody RateUpdateRequest request) {

        // Update only non-null fields
        if (request.getElectricityRate() != null) {
            rateConfig.setElectricityRate(request.getElectricityRate());
        }

        if (request.getWaterRate() != null) {
            rateConfig.setWaterRate(request.getWaterRate());
        }

        if (request.getMotorbikeRate() != null) {
            rateConfig.setMotorbikeRate(request.getMotorbikeRate());
        }

        if (request.getBikeRate() != null) {
            rateConfig.setBikeRate(request.getBikeRate());
        }

        return ResponseEntity.ok(rateConfig);
    }
}