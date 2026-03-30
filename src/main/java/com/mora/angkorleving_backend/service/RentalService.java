package com.mora.angkorleving_backend.service;

//package com.mora.backendangkorliving.service;

import com.mora.angkorleving_backend.DTOs.request.RentalRequest;
import com.mora.angkorleving_backend.DTOs.response.RentalResponse;

import java.util.List;

public interface RentalService {
    RentalResponse createRental(RentalRequest request);
    RentalResponse getRental(Long id);
    List<RentalResponse> getAllRentals();
    void deleteRental(Long id);
}


