package com.mora.angkorleving_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//@Entity
//@Table(name = "payments")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Payment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private LocalDate dueDate;      // ថ្ងៃត្រូវបង់
//    private LocalDate paymentDate;  // ថ្ងៃបានបង់
//    private Double amount;
//
//    @Column(nullable=true)
//    private String method; // CASH, QR
//
//    @Column(nullable=false)
//    private String status; // PAID, PENDING, LATE
//
//    @Column(columnDefinition = "TEXT")
//    private String qrCode; // KHQR string
//
//    private String qrMd5;  // NBC md5 hash
//
//    @ManyToOne
//    @JoinColumn(name = "rental_id", nullable=false)
//    private Rental rental;
//
//    @ManyToOne
//    @JoinColumn(name = "expense_id", nullable=true)
//    private Expense expense; // link to monthly expense (nullable for first month)
//}
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Double amount;

    @Column(nullable = true)
    private String method; // CASH, QR

    @Column(nullable = false)
    private String status; // PAID, PENDING, LATE

    @Column(columnDefinition = "TEXT")
    private String qrCode;

    private String qrMd5;

    // ✅ ADD THESE TWO
    private String currency;
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = true)
    private Expense expense;
}