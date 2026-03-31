package com.mora.angkorleving_backend.Repository;


import com.mora.angkorleving_backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT MAX(r.roomNumber) FROM Room r WHERE r.floor.id = :floorId")
    Integer findMaxRoomNumberByFloorId(Long floorId);
}

