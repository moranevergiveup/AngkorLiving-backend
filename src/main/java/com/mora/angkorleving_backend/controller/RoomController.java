package com.mora.angkorleving_backend.controller;



import com.mora.angkorleving_backend.DTOs.request.RoomRequest;
import com.mora.angkorleving_backend.DTOs.response.RoomResponse;
import com.mora.angkorleving_backend.service.RoomService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    //  Create Room (with optional image upload)
    @PostMapping(consumes = {"multipart/form-data"})
    public RoomResponse createRoom(@ModelAttribute RoomRequest request,
                                   @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return roomService.createRoom(request, imageFile);
    }

    //  Get Room by ID
    @GetMapping("/{id}")
    public RoomResponse getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }

    // Get All Rooms
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    //  Update Room (with optional new image upload)
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public RoomResponse updateRoom(@PathVariable Long id,
                                   @ModelAttribute RoomRequest request,
                                   @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return roomService.updateRoom(id, request, imageFile);
    }

    //  Delete Room
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}

