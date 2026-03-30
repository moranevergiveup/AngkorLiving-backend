package com.mora.angkorleving_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RateConfig {
    private double electricityRate = 0.5;
    private double waterRate = 0.2;
    private double motorbikeRate = 10;
    private double bikeRate = 5;
}