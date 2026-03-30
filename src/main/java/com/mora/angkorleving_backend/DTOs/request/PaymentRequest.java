//package com.mora.angkorleving_backend.DTOs.request;
//
//import lombok.Data;
//
//@Data
//public class PaymentRequest {
//    private Long rentalId;
//    private Double amount;
//    private String method; // CASH or QR
//}
package com.mora.angkorleving_backend.DTOs.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    private Long rentalId;
    private LocalDate dueDate;      // optional
    private String method;          // CASH, QR
}
