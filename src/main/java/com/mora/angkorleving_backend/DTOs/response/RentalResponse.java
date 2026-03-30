package com.mora.angkorleving_backend.DTOs.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private String roomNumber;
    private String tenantEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Double monthlyRent;
}

