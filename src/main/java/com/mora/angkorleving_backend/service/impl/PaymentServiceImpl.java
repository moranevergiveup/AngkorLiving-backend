//package com.mora.angkorleving_backend.service;
//
//import com.mora.angkorleving_backend.DTOs.request.PaymentRequest;
//import com.mora.angkorleving_backend.DTOs.response.PaymentRespone;
//import com.mora.angkorleving_backend.DTOs.response.PaymentResponse;
//import com.mora.angkorleving_backend.Repository.PaymentRepository;
//import com.mora.angkorleving_backend.Repository.RentalRepository;
//import com.mora.angkorleving_backend.model.Payment;
//import com.mora.angkorleving_backend.model.Rental;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class PaymentServiceImpl implements PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final RentalRepository rentalRepository;
//
//    public PaymentServiceImpl(PaymentRepository paymentRepository, RentalRepository rentalRepository) {
//        this.paymentRepository = paymentRepository;
//        this.rentalRepository = rentalRepository;
//    }
//
//    @Override
//    public PaymentResponse payMonthly(PaymentRequest request) {
//        Rental rental = rentalRepository.findById(request.getRentalId())
//                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
//
//        // Find latest payment for this rental
//        Payment latestPayment = paymentRepository.findTopByRentalOrderByDueDateDesc(rental)
//                .orElseThrow(() -> new EntityNotFoundException("No payment record found"));
//
//        // Update latest payment to PAID
//        latestPayment.setPaymentDate(LocalDate.now());
//        latestPayment.setMethod(request.getMethod());
//        latestPayment.setStatus("PAID");
//        paymentRepository.save(latestPayment);
//
//        // Extend rental endDate by 30 days
//        rental.setEndDate(rental.getEndDate().plusDays(30));
//        rentalRepository.save(rental);
//
//        // Generate next payment record
//        Payment nextPayment = Payment.builder()
//                .rental(rental)
//                .dueDate(rental.getEndDate())
//                .amount(rental.getMonthlyRent())
//                .status("PENDING")
//                .build();
//        paymentRepository.save(nextPayment);
//
//        return new PaymentResponse(latestPayment.getId(), latestPayment.getDueDate(),
//                latestPayment.getPaymentDate(), latestPayment.getAmount(),
//                latestPayment.getMethod(), latestPayment.getStatus());
//    }
//    @Override
//    public List<PaymentRespone> getAllPayments() {
//        return paymentRepository.findAll().stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    //Helper method to convert entity → DTO
//    private PaymentRespone mapToResponse(Payment p) {
//        return new PaymentRespone(
//                p.getId(),
//                p.getRental().getTenant().getUsername(),   // tenantName from Rental entity
//                p.getRental().getRoom().getRoomNumber(),
//                p.getAmount(),
//                p.getStatus(),
//                p.getPaymentDate() != null ? p.getPaymentDate().toString() : null
//        );
//    }
//}
//
package com.mora.angkorleving_backend.service.impl;

import com.mora.angkorleving_backend.DTOs.request.PaymentRequest;
import com.mora.angkorleving_backend.DTOs.response.PaymentResponse;
import com.mora.angkorleving_backend.Repository.PaymentRepository;
import com.mora.angkorleving_backend.Repository.RentalRepository;
import com.mora.angkorleving_backend.Repository.ExpenseRepository;
import com.mora.angkorleving_backend.model.Payment;
import com.mora.angkorleving_backend.model.Rental;
import com.mora.angkorleving_backend.model.Expense;
import com.mora.angkorleving_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final ExpenseRepository expenseRepository;

    @Override
//    public PaymentResponse createPayment(PaymentRequest request) {
//        Rental rental = rentalRepository.findById(request.getRentalId())
//                .orElseThrow(() -> new RuntimeException("Rental not found"));
//
//        LocalDate dueDate = (request.getDueDate() != null) ? request.getDueDate() : LocalDate.now().plusMonths(1);
//
//        boolean isFirstPayment = paymentRepository.findAllByRental(rental).isEmpty();
//
//        double amount;
//        Expense expense = null;
//
//        if (isFirstPayment) {
//            amount = rental.getMonthlyRent();
//        } else {
//            expense = expenseRepository.findByRoomAndMonthAndYear(
//                    rental.getRoom(),
//                    dueDate.getMonthValue(),
//                    dueDate.getYear()
//            ).orElse(null);
//
//            double expenseAmount = (expense != null) ? expense.getTotalExpense() : 0;
//            amount = rental.getMonthlyRent() + expenseAmount;
//        }
//
//        Payment payment = Payment.builder()
//                .rental(rental)
//                .expense(expense)
//                .dueDate(dueDate)
//                .status("PENDING")
//                .amount(amount)
//                .method(request.getMethod())
//                .build();
//
//        Payment saved = paymentRepository.save(payment);
//
//        return toResponse(saved);
//    }
    public PaymentResponse createPayment(PaymentRequest request) {

        // 1. Get rental
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // 2. Determine due date
        LocalDate dueDate = (request.getDueDate() != null)
                ? request.getDueDate()
                : LocalDate.now();

        int month = dueDate.getMonthValue();
        int year = dueDate.getYear();

        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        boolean exists = paymentRepository.existsByRentalAndDueDateBetween(
                rental,
                startOfMonth,
                endOfMonth
        );

        // 4. Get expense (if exists)
        Expense expense = expenseRepository
                .findByRoomAndMonthAndYear(rental.getRoom(), month, year)
                .orElse(null);

        double rentAmount = rental.getMonthlyRent();
        double expenseAmount = (expense != null) ? expense.getTotalExpense() : 0;

        // 5. Calculate total
        double totalAmount = rentAmount + expenseAmount;

        // 6. Create payment
        Payment payment = Payment.builder()
                .rental(rental)
                .expense(expense) // null for first month (OK)
                .dueDate(dueDate)
                .status("PENDING")
                .amount(totalAmount)
                .method(request.getMethod())
                .build();

        Payment saved = paymentRepository.save(payment);

        return toResponse(saved);
    }
    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse res = new PaymentResponse();
        res.setId(payment.getId());
        res.setRentalId(payment.getRental().getId());
        res.setDueDate(payment.getDueDate());
        res.setPaymentDate(payment.getPaymentDate());
        res.setAmount(payment.getAmount());
        res.setMethod(payment.getMethod());
        res.setStatus(payment.getStatus());
        res.setQrCode(payment.getQrCode());
        res.setQrMd5(payment.getQrMd5());

        if (payment.getExpense() != null) {
            res.setExpenseId(payment.getExpense().getId());
            res.setElectricityFee(payment.getExpense().getElectricityFee());
            res.setWaterFee(payment.getExpense().getWaterFee());
            res.setTrashFee(payment.getExpense().getTrashFee());
            res.setVehicleFee(payment.getExpense().getVehicleFee());
            res.setMonth(payment.getExpense().getMonth());
            res.setYear(payment.getExpense().getYear());
        }

        return res;
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        return paymentRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<PaymentResponse> getPaymentsByRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));
        return paymentRepository.findAllByRental(rental).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        if (status.equalsIgnoreCase("PAID")) {
            payment.setPaymentDate(LocalDate.now());
        }
        return toResponse(paymentRepository.save(payment));
    }
    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse) // convert entity → DTO
                .toList();
    }

}
