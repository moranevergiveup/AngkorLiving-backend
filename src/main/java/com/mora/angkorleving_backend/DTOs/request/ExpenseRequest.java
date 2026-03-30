package com.mora.angkorleving_backend.DTOs.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {
    private Long roomId;
    private int month;
    private int year;

    private int electricityNew;
    private int waterNew;

    private double trashFee;

    private int motorbikeCount;
    private int bikeCount;
}