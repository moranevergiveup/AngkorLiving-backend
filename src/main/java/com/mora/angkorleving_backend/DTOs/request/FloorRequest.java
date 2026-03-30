package com.mora.angkorleving_backend.DTOs.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FloorRequest {
    @NotBlank(message = "Floor name is required")
    private String name;
    private String description;
    @NotNull(message = "Price is required")
    private Double price;
    private Integer numberOfRooms;
}

