package com.mora.angkorleving_backend.controller;

import com.mora.angkorleving_backend.DTOs.request.RentalRequest;
import com.mora.angkorleving_backend.DTOs.response.RentalResponse;
import com.mora.angkorleving_backend.service.RentalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public RentalResponse createRental(@RequestBody RentalRequest request) {
        return rentalService.createRental(request);
    }

    @GetMapping("/{id}")
    public RentalResponse getRental(@PathVariable Long id) {
        return rentalService.getRental(id);
    }

    @GetMapping
    public List<RentalResponse> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
    }
}

