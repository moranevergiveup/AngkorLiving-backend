package com.mora.angkorleving_backend.service.impl;

import com.mora.angkorleving_backend.DTOs.request.ExpenseRequest;
import com.mora.angkorleving_backend.DTOs.response.ExpenseResponse;
import com.mora.angkorleving_backend.Repository.ExpenseRepository;
import com.mora.angkorleving_backend.Repository.RoomRepository;
import com.mora.angkorleving_backend.config.RateConfig;
import com.mora.angkorleving_backend.model.Expense;
import com.mora.angkorleving_backend.model.Room;
import com.mora.angkorleving_backend.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final RoomRepository roomRepository;
    private final RateConfig rateConfig;

    @Override
//    public ExpenseResponse createExpense(ExpenseRequest req) {
//        Room room = roomRepository.findById(req.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        // Prevent duplicate
//        expenseRepository.findByRoomAndMonthAndYear(room, req.getMonth(), req.getYear())
//                .ifPresent(e -> { throw new RuntimeException("Expense already exists for this month"); });
//
//        // Get last expense for old meter readings
//        Expense lastExpense = expenseRepository.findTopByRoomOrderByYearDescMonthDesc(room).orElse(null);
//        int electricityOld = lastExpense != null ? lastExpense.getElectricityNew() : 0;
//        int waterOld = lastExpense != null ? lastExpense.getWaterNew() : 0;
//
//        // Calculate fees
//        double electricityFee = (req.getElectricityNew() - electricityOld) * rateConfig.getElectricityRate();
//        double waterFee = (req.getWaterNew() - waterOld) * rateConfig.getWaterRate();
//        double vehicleFee = req.getMotorbikeCount() * rateConfig.getMotorbikeRate()
//                + req.getBikeCount() * rateConfig.getBikeRate();
//        double totalExpense = electricityFee + waterFee + req.getTrashFee() + vehicleFee;
//
//        Expense expense = Expense.builder()
//                .room(room)
//
//                .month(req.getMonth())
//                .year(req.getYear())
//                .electricityOld(electricityOld)
//                .electricityNew(req.getElectricityNew())
//                .electricityFee(electricityFee)
//                .waterOld(waterOld)
//                .waterNew(req.getWaterNew())
//                .waterFee(waterFee)
//                .trashFee(req.getTrashFee())
//                .motorbikeCount(req.getMotorbikeCount())
//                .bikeCount(req.getBikeCount())
//                .vehicleFee(vehicleFee)
//                .totalExpense(totalExpense)
//                .build();
//
//        return toResponse(expenseRepository.save(expense));
//    }
    public ExpenseResponse createExpense(ExpenseRequest req) {

        Room room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // ❌ Prevent duplicate month
        expenseRepository.findByRoomAndMonthAndYear(room, req.getMonth(), req.getYear())
                .ifPresent(e -> {
                    throw new RuntimeException("Expense already exists for this month");
                });

        // ✅ Get last record (previous month)
        Expense lastExpense = expenseRepository
                .findTopByRoomOrderByYearDescMonthDesc(room)
                .orElse(null);

        int electricityOld = 0;
        int waterOld = 0;

        if (lastExpense != null) {
            electricityOld = lastExpense.getElectricityNew();
            waterOld = lastExpense.getWaterNew();
        }

        // ❌ VALIDATION (IMPORTANT)
        if (req.getElectricityNew() < electricityOld) {
            throw new RuntimeException("Electricity new reading cannot be less than old reading");
        }

        if (req.getWaterNew() < waterOld) {
            throw new RuntimeException("Water new reading cannot be less than old reading");
        }

        // ✅ USAGE
        int electricityUsage = req.getElectricityNew() - electricityOld;
        int waterUsage = req.getWaterNew() - waterOld;

        // ✅ FEES
        double electricityFee = electricityUsage * rateConfig.getElectricityRate();
        double waterFee = waterUsage * rateConfig.getWaterRate();

        double vehicleFee =
                req.getMotorbikeCount() * rateConfig.getMotorbikeRate() +
                        req.getBikeCount() * rateConfig.getBikeRate();

        double totalExpense =
                electricityFee +
                        waterFee +
                        req.getTrashFee() +
                        vehicleFee;

        Expense expense = Expense.builder()
                .room(room)
                .month(req.getMonth())
                .year(req.getYear())

                // Electricity
                .electricityOld(electricityOld)
                .electricityNew(req.getElectricityNew())
                .electricityFee(electricityFee)

                // Water
                .waterOld(waterOld)
                .waterNew(req.getWaterNew())
                .waterFee(waterFee)

                // Others
                .trashFee(req.getTrashFee())
                .motorbikeCount(req.getMotorbikeCount())
                .bikeCount(req.getBikeCount())
                .vehicleFee(vehicleFee)

                .totalExpense(totalExpense)
                .build();

        return toResponse(expenseRepository.save(expense));
    }

    private ExpenseResponse toResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .roomNumber(expense.getRoom().getRoomNumber())
                .month(expense.getMonth())
                .year(expense.getYear())
                .electricityOld(expense.getElectricityOld())
                .electricityNew(expense.getElectricityNew())
                .electricityFee(expense.getElectricityFee())
                .waterOld(expense.getWaterOld())
                .waterNew(expense.getWaterNew())
                .waterFee(expense.getWaterFee())
                .trashFee(expense.getTrashFee())
                .motorbikeCount(expense.getMotorbikeCount())
                .bikeCount(expense.getBikeCount())
                .vehicleFee(expense.getVehicleFee())
                .totalExpense(expense.getTotalExpense())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {
        return expenseRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    @Override
    public List<ExpenseResponse> getExpensesByRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return expenseRepository.findAllByRoom(room).stream().map(this::toResponse).toList();
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest req) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        int electricityOld = expense.getElectricityOld();
        int waterOld = expense.getWaterOld();

        double electricityFee = (req.getElectricityNew() - electricityOld) * rateConfig.getElectricityRate();
        double waterFee = (req.getWaterNew() - waterOld) * rateConfig.getWaterRate();
        double vehicleFee = req.getMotorbikeCount() * rateConfig.getMotorbikeRate()
                + req.getBikeCount() * rateConfig.getBikeRate();
        double totalExpense = electricityFee + waterFee + req.getTrashFee() + vehicleFee;

        expense.setElectricityNew(req.getElectricityNew());
        expense.setElectricityFee(electricityFee);
        expense.setWaterNew(req.getWaterNew());
        expense.setWaterFee(waterFee);
        expense.setTrashFee(req.getTrashFee());
        expense.setMotorbikeCount(req.getMotorbikeCount());
        expense.setBikeCount(req.getBikeCount());
        expense.setVehicleFee(vehicleFee);
        expense.setTotalExpense(totalExpense);

        return toResponse(expenseRepository.save(expense));
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found");
        }
        expenseRepository.deleteById(id);
    }
}