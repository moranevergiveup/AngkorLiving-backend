package com.mora.angkorleving_backend.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String floorName;
    private Double price;
    private String status;
    private String imageUrl;
}
