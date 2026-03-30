package com.mora.angkorleving_backend.service.impl;

import com.mora.angkorleving_backend.DTOs.request.FloorRequest;
import com.mora.angkorleving_backend.DTOs.response.FloorResponse;
import com.mora.angkorleving_backend.Repository.FloorRepository;
import com.mora.angkorleving_backend.model.Floor;
import com.mora.angkorleving_backend.service.FloorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;

    public FloorServiceImpl(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    @Override
    public FloorResponse createFloor(FloorRequest request) {
        Floor floor = Floor.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
        Floor saved = floorRepository.save(floor);
        return new FloorResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), saved.getNumberOfRooms());
    }

//    @Override
    @Override
    public List<FloorResponse> getAllFloors() {
        return floorRepository.findAllByOrderByIdAsc()
                .stream()
                .map(f -> new FloorResponse(f.getId(), f.getName(), f.getDescription(), f.getPrice(),f.getNumberOfRooms()))
                .collect(Collectors.toList());
    }


    @Override
    public FloorResponse getFloorById(Long id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));
        return new FloorResponse(floor.getId(), floor.getName(), floor.getDescription(), floor.getPrice(),floor.getNumberOfRooms());
    }

    @Override
//    public FloorResponse updateFloor(Long id, FloorRequest request) {
//        Floor floor = floorRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));
//
//        if (request.getName() != null && !request.getName().isBlank()) {
//            floor.setName(request.getName());
//        }
//        if (request.getDescription() != null) {
//            floor.setDescription(request.getDescription());
//        }
//        if (request.getPrice() != null) {
//            floor.setPrice(request.getPrice());
//        }
//
//        Floor updated = floorRepository.save(floor);
//        return new FloorResponse(updated.getId(), updated.getName(), updated.getDescription(), updated.getPrice());
//    }



//    @Transactional
//    public FloorResponse updateFloor(Long id, FloorRequest request) {
//        Floor floor = floorRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));
//
//        // ✅ update only provided fields
//        if (request.getName() != null && !request.getName().isBlank()) {
//            floor.setName(request.getName());
//        }
//        if (request.getDescription() != null && !request.getDescription().isBlank()) {
//            floor.setDescription(request.getDescription());
//        }
//        if (request.getPrice() != null) {
//            floor.setPrice(request.getPrice());
//        }
//
//        // ✅ save will update existing row, not create new one
//        Floor updated = floorRepository.save(floor);
//
//        return new FloorResponse(
//                updated.getId(),
//                updated.getName(),
//                updated.getDescription(),
//                updated.getPrice()
//        );
//    }


    @Transactional
    public FloorResponse updateFloor(Long id, FloorRequest request) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));

        // ✅ update only provided fields
        if (request.getName() != null && !request.getName().isBlank()) {
            floor.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            floor.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            floor.setPrice(request.getPrice());
        }

        // ✅ save will update existing row, not create new one
        Floor updated = floorRepository.saveAndFlush(floor);

        return new FloorResponse(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getPrice(),
                updated.getNumberOfRooms()
        );
    }




    @Override
    public void deleteFloor(Long id) {
        if (!floorRepository.existsById(id)) {
            throw new EntityNotFoundException("Floor not found with id: " + id);
        }
        floorRepository.deleteById(id);
    }
}

