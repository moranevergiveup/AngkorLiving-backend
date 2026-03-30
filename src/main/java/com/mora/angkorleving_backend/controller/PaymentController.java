package com.mora.angkorleving_backend.controller;
import com.mora.angkorleving_backend.DTOs.request.PaymentRequest;
import com.mora.angkorleving_backend.DTOs.response.BakongResponse;
import com.mora.angkorleving_backend.DTOs.response.PaymentResponse;
import com.mora.angkorleving_backend.model.Payment;
import com.mora.angkorleving_backend.service.PaymentService;
import com.mora.angkorleving_backend.service.impl.QrPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final QrPaymentService qrPaymentService;
    private final PaymentService paymentService;

    public PaymentController(QrPaymentService qrPaymentService,PaymentService paymentService) {
        this.qrPaymentService = qrPaymentService;
        this.paymentService=paymentService;
    }
    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate/{paymentId}")
    public ResponseEntity<BakongResponse> generateQr(@PathVariable Long paymentId) {
        Payment payment = qrPaymentService.generateQr(paymentId);

        BakongResponse response = new BakongResponse();
        response.setMd5(payment.getQrMd5());
        response.setQr(payment.getQrCode());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency() != null ? payment.getCurrency() : "USD");
        response.setStatus(payment.getStatus());
        response.setTimestamp(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null);
        response.setTransactionId(payment.getTransactionId());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify/{md5}")
    public ResponseEntity<BakongResponse> verifyPayment(@PathVariable String md5) {
        Payment payment = qrPaymentService.verifyPayment(md5);
        BakongResponse response = new BakongResponse();
        response.setMd5(payment.getQrMd5());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency() != null ? payment.getCurrency() : "USD");
        response.setStatus(payment.getStatus());
        response.setTimestamp(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null);
        response.setTransactionId(payment.getTransactionId());
        response.setQr(payment.getQrCode());

        return ResponseEntity.ok(response);
    }
}
