package com.mora.angkorleving_backend.service.impl;

//package com.mora.backendangkorliving.service.impl;

import com.mora.angkorleving_backend.DTOs.request.RentalRequest;
import com.mora.angkorleving_backend.DTOs.response.RentalResponse;
import com.mora.angkorleving_backend.Repository.PaymentRepository;
import com.mora.angkorleving_backend.Repository.RentalRepository;
import com.mora.angkorleving_backend.Repository.RoomRepository;
import com.mora.angkorleving_backend.Repository.UserRepository;
import com.mora.angkorleving_backend.model.Payment;
import com.mora.angkorleving_backend.model.Rental;
import com.mora.angkorleving_backend.model.Room;
import com.mora.angkorleving_backend.model.User;
import com.mora.angkorleving_backend.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public RentalServiceImpl(RentalRepository rentalRepository, RoomRepository roomRepository, UserRepository userRepository,PaymentRepository paymentRepository) {
        this.rentalRepository = rentalRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

//    @Override
//    public RentalResponse createRental(RentalRequest request) {
//        Room room = roomRepository.findById(request.getRoomId())
//                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
//        User tenant = userRepository.findById(request.getTenantId())
//                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
//
//        LocalDate endDate = request.getStartDate().plusDays(30);
//
//        Rental rental = Rental.builder()
//                .room(room)
//                .tenant(tenant)
//                .startDate(request.getStartDate())
//                .endDate(endDate)
//                .status("PENDING")
//                .monthlyRent(room.getPrice())
//                .build();
//
//        Rental saved = rentalRepository.save(rental);
//        return new RentalResponse(saved.getId(), room.getRoomNumber(), tenant.getEmail(),
//                saved.getStartDate(), saved.getEndDate(), saved.getStatus(), saved.getMonthlyRent());
//    }
    @Override
//    public RentalResponse createRental(RentalRequest request) {
//        Room room = roomRepository.findById(request.getRoomId())
//                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
//        User tenant = userRepository.findById(request.getTenantId())
//                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));
//
//        LocalDate endDate = request.getStartDate().plusDays(30);
//
//        Rental rental = Rental.builder()
//                .room(room)
//                .tenant(tenant)
//                .startDate(request.getStartDate())
//                .endDate(endDate)
//                .status("ACTIVE")
//                .monthlyRent(room.getPrice())
//                .build();
//
//        Rental saved = rentalRepository.save(rental);
//
//        // ✅ Use instance variable, not class name
//        Payment payment = Payment.builder()
//                .rental(saved)
//                .dueDate(saved.getEndDate())
//                .amount(saved.getMonthlyRent())
//                .status("PENDING")
//                .build();
//
//        paymentRepository.save(payment); // correct usage
//
//        return new RentalResponse(saved.getId(), room.getRoomNumber(), tenant.getEmail(),
//                saved.getStartDate(), saved.getEndDate(), saved.getStatus(), saved.getMonthlyRent());
//    }
    public RentalResponse createRental(RentalRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        User tenant = userRepository.findById(request.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found"));

        // 🔥 Check if room is already rented
        if ("RENTED".equalsIgnoreCase(room.getStatus())) {
            throw new RuntimeException("Room is already rented");
        }

        LocalDate endDate = request.getStartDate().plusDays(30);

        Rental rental = Rental.builder()
                .room(room)
                .tenant(tenant)
                .startDate(request.getStartDate())
                .endDate(endDate)
                .status("ACTIVE")
                .monthlyRent(room.getPrice())
                .build();

        Rental saved = rentalRepository.save(rental);

        // ✅ UPDATE ROOM STATUS HERE
        room.setStatus("RENTED"); // or "OCCUPIED"
        roomRepository.save(room);

        Payment payment = Payment.builder()
                .rental(saved)
                .dueDate(saved.getEndDate())
                .amount(saved.getMonthlyRent())
                .status("PENDING")
                .build();

        paymentRepository.save(payment);

        return new RentalResponse(
                saved.getId(),
                room.getRoomNumber(),
                tenant.getEmail(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getStatus(),
                saved.getMonthlyRent()
        );
    }
    @Override
    public RentalResponse getRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        return new RentalResponse(rental.getId(), rental.getRoom().getRoomNumber(),
                rental.getTenant().getEmail(), rental.getStartDate(), rental.getEndDate(),
                rental.getStatus(), rental.getMonthlyRent());
    }

    @Override
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(r -> new RentalResponse(r.getId(), r.getRoom().getRoomNumber(),
                        r.getTenant().getEmail(), r.getStartDate(), r.getEndDate(),
                        r.getStatus(), r.getMonthlyRent()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRental(Long id) {
        if (!rentalRepository.existsById(id)) {
            throw new EntityNotFoundException("Rental not found");
        }
        rentalRepository.deleteById(id);
    }
}
