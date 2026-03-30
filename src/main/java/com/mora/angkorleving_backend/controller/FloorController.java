package com.mora.angkorleving_backend.controller;

import com.mora.angkorleving_backend.DTOs.request.FloorRequest;
import com.mora.angkorleving_backend.DTOs.response.FloorResponse;
import com.mora.angkorleving_backend.service.FloorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floors")
public class FloorController {

    @Autowired
    private FloorService floorService;

    @PostMapping
    public FloorResponse createFloor(@Valid @RequestBody FloorRequest request) {
        return floorService.createFloor(request);
    }

    @GetMapping
    public List<FloorResponse> getAllFloors() {
        return floorService.getAllFloors();
    }

    @GetMapping("/{id}")
    public FloorResponse getFloorById(@PathVariable Long id) {
        return floorService.getFloorById(id);
    }

    @PutMapping("/{id}")
    public FloorResponse updateFloor(@PathVariable Long id, @Valid @RequestBody FloorRequest request) {
        return floorService.updateFloor(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteFloor(@PathVariable Long id) {
        floorService.deleteFloor(id);
    }
}

