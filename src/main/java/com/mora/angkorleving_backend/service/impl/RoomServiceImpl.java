package com.mora.angkorleving_backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mora.angkorleving_backend.DTOs.request.RoomRequest;
import com.mora.angkorleving_backend.DTOs.response.RoomResponse;
import com.mora.angkorleving_backend.Repository.FloorRepository;
import com.mora.angkorleving_backend.Repository.RoomRepository;
import com.mora.angkorleving_backend.model.Floor;
import com.mora.angkorleving_backend.model.Room;
import com.mora.angkorleving_backend.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final Cloudinary cloudinary;

    public RoomServiceImpl(RoomRepository roomRepository, FloorRepository floorRepository, Cloudinary cloudinary) {
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public RoomResponse createRoom(RoomRequest request, MultipartFile imageFile) throws IOException {

        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new EntityNotFoundException("Floor not found"));

        Integer nextRoomNumber = generateNextRoomNumber(request.getFloorId());

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader()
                    .upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            imageUrl = (String) uploadResult.get("url");
        }

        Room room = Room.builder()
                .roomNumber(nextRoomNumber.toString())
                .floor(floor)
                .status(request.getStatus())
                .imageUrl(imageUrl)
                .build();

        Room saved = roomRepository.save(room);
        return toResponse(saved);
    }

    public Integer generateNextRoomNumber(Long floorId) {
        Integer maxRoom = roomRepository.findMaxRoomNumberByFloorId(floorId);

        if (maxRoom == null) {
            return (int) (floorId * 100 + 1); // 1 → 101
        }

        return maxRoom + 1;
    }
    @Override
    public RoomResponse getRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
        return toResponse(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request, MultipartFile imageFile) throws IOException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

//        if (request.getRoomNumber() != null) {
//            room.setRoomNumber(request.getRoomNumber());
//        }
        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        }
        if (request.getFloorId() != null) {
            Floor floor = floorRepository.findById(request.getFloorId())
                    .orElseThrow(() -> new EntityNotFoundException("Floor not found"));
            room.setFloor(floor);
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            room.setImageUrl((String) uploadResult.get("url"));
        }

        Room updated = roomRepository.save(room);
        return toResponse(updated);
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getFloor().getName(),
                room.getPrice(),
                room.getStatus(),
                room.getImageUrl()
        );
    }
}
