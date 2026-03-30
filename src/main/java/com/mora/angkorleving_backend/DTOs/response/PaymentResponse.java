//package com.mora.angkorleving_backend.DTOs.response;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//@AllArgsConstructor
//public class PaymentResponse {
//    private Long id;
//    private LocalDate dueDate;
//    private LocalDate paymentDate;
//    private Double amount;
//    private String method;
//    private String status;
//}

package com.mora.angkorleving_backend.DTOs.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentResponse {

    private Long id;
    private Long rentalId;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Double amount;
    private String method;
    private String status;
    private String qrCode;
    private String qrMd5;

    private Long expenseId;
    private Double electricityFee;
    private Double waterFee;
    private Double trashFee;
    private Double vehicleFee;
    private Integer month;
    private Integer year;
}