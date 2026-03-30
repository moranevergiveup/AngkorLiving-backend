package com.mora.angkorleving_backend.Repository;

import com.mora.angkorleving_backend.model.Expense;
import com.mora.angkorleving_backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Find an expense by room + month + year to prevent duplicates
    Optional<Expense> findByRoomAndMonthAndYear(Room room, int month, int year);

    // Find the latest expense for a room (to get old readings)
    Optional<Expense> findTopByRoomOrderByYearDescMonthDesc(Room room);

    // Find all expenses of a room
    List<Expense> findAllByRoom(Room room);
}