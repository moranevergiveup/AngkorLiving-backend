package com.mora.angkorleving_backend.DTOs.request;

import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private Long floorId;
    private String status;
}

