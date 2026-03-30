package com.mora.angkorleving_backend.service;

import com.mora.angkorleving_backend.DTOs.request.FloorRequest;
import com.mora.angkorleving_backend.DTOs.response.FloorResponse;

import java.util.List;

public interface FloorService {
    FloorResponse createFloor(FloorRequest request);
    List<FloorResponse> getAllFloors();
    FloorResponse getFloorById(Long id);
    FloorResponse updateFloor(Long id, FloorRequest request);
    void deleteFloor(Long id);
}

