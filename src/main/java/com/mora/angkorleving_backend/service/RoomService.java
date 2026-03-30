package com.mora.angkorleving_backend.service;

import com.mora.angkorleving_backend.DTOs.request.RoomRequest;
import com.mora.angkorleving_backend.DTOs.response.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest request, MultipartFile imageFile) throws IOException;
    RoomResponse getRoom(Long id);
    List<RoomResponse> getAllRooms();
    RoomResponse updateRoom(Long id, RoomRequest request, MultipartFile imageFile) throws IOException;
    void deleteRoom(Long id);
}


