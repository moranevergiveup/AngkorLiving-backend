package com.mora.angkorleving_backend.DTOs.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RateUpdateRequest {

    @Positive(message = "Electricity rate must be positive")
    private Double electricityRate; // nullable, only validate if provided

    @Positive(message = "Water rate must be positive")
    private Double waterRate;      // nullable, only validate if provided

    @Positive(message = "Motorbike rate must be positive")
    private Double motorbikeRate;  // nullable, only validate if provided

    @Positive(message = "Bike rate must be positive")
    private Double bikeRate;       // nullable, only validate if provided
}