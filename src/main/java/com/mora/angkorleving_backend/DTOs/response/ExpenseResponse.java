package com.mora.angkorleving_backend.DTOs.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {
    private Long id;
    private String roomNumber;

    private int month;
    private int year;

    private int electricityOld;
    private int electricityNew;
    private double electricityFee;

    private int waterOld;
    private int waterNew;
    private double waterFee;

    private double trashFee;

    private int motorbikeCount;
    private int bikeCount;
    private double vehicleFee;

    private double totalExpense;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}