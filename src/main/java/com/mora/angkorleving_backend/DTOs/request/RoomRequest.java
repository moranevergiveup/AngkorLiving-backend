package com.mora.angkorleving_backend.DTOs.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RoomRequest {

    @Column(nullable=true)
    private Long floorId;
    private String status;
}

