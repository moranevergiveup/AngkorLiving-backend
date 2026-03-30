//package com.mora.angkorleving_backend.service;
//
//import com.mora.angkorleving_backend.DTOs.request.PaymentRequest;
//import com.mora.angkorleving_backend.DTOs.response.PaymentRespone;
//import com.mora.angkorleving_backend.DTOs.response.PaymentResponse;
//
//import java.util.List;
//
//public interface PaymentService {
//    PaymentResponse payMonthly(PaymentRequest request);
//    // ✅ New method to get all payments
////    List<PaymentResponse> getAllPayments();
////    // ✅ Get payment by ID
////    PaymentResponse getPaymentById(Long id);
//    List<PaymentRespone> getAllPayments();
//}
package com.mora.angkorleving_backend.service;

import com.mora.angkorleving_backend.DTOs.request.PaymentRequest;
import com.mora.angkorleving_backend.DTOs.response.PaymentResponse;
import com.mora.angkorleving_backend.model.Payment;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getPaymentsByRental(Long rentalId);
    PaymentResponse updatePaymentStatus(Long id, String status);
    List<PaymentResponse> getAllPayments();
//    public Payment createSecondMonthPayment(Long rentalId);
}

