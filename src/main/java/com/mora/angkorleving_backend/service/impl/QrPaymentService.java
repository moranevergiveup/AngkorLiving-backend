
package com.mora.angkorleving_backend.service.impl;

import com.mora.angkorleving_backend.Repository.PaymentRepository;
import com.mora.angkorleving_backend.model.Payment;
import com.mora.angkorleving_backend.service.BakongTokenService;
import jakarta.persistence.EntityNotFoundException;
import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;
import lombok.RequiredArgsConstructor;
//import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrPaymentService {

    private final PaymentRepository paymentRepository;
    private final BakongTokenService bakongTokenService;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${BAKONG_API_TOKEN}")
    private String bakongToken;
    @Value("${BAKONG_ACCOUNT-ID}")
    private String username ;
    @Value("${BAKONG_ACCOUNT_NAME}")
    private String account_name ;
    @Value("${BAKONG_LOCATION}")
    private String location;
    @Value("${bakong.verify-url}")
    private String verify_url;


    public Payment generateQr(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if ("PAID".equalsIgnoreCase(payment.getStatus())) {
            throw new RuntimeException("Payment already completed");
        }

        // Always generate new KHQR
        IndividualInfo info = new IndividualInfo();
        info.setBakongAccountId(username); // or inject from properties
        info.setCurrency(KHQRCurrency.USD);
        info.setAmount(0.01);
        info.setMerchantName(account_name);
        info.setMerchantCity(location);

        info.setBillNumber("PAY-" + payment.getId() + "-" + System.currentTimeMillis());
        info.setPurposeOfTransaction("Rental payment");
        info.setExpirationTimestamp(System.currentTimeMillis() + 15 * 60 * 1000); // 15 min

        // Generate KHQR
        KHQRResponse<KHQRData> response = BakongKHQR.generateIndividual(info);

        if (response.getKHQRStatus().getCode() != 0 || response.getData().getQr() == null) {
            throw new RuntimeException("KHQR generation failed: " + response.getKHQRStatus().getMessage());
        }

        // Update payment with fresh QR
        payment.setQrCode(response.getData().getQr());
        payment.setQrMd5(response.getData().getMd5());
        payment.setMethod("QR");
        payment.setStatus("PENDING");

        return paymentRepository.save(payment);
    }
    public Payment verifyPayment(String md5) {
        Payment payment = paymentRepository.findByQrMd5(md5);
        if (payment == null) {
            throw new EntityNotFoundException("Payment not found for md5: " + md5);
        }

        if ("PAID".equalsIgnoreCase(payment.getStatus())) {
            return payment;
        }

        String token = bakongTokenService.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, String> body = new HashMap<>();
        body.put("md5", md5);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(verify_url, request, Map.class);
            Map<String, Object> responseMap = responseEntity.getBody();

            if (responseMap == null || !responseMap.containsKey("responseCode")) {
                throw new RuntimeException("Invalid response from Bakong API");
            }

            Integer responseCode = (Integer) responseMap.get("responseCode");
            Map<String, Object> data = (Map<String, Object>) responseMap.get("data");

            if (responseCode != 0 || data == null) {
                return payment; // payment not completed yet
            }

            // Update payment
            payment.setStatus("PAID");
            payment.setTransactionId((String) data.get("hash"));
            payment.setAmount(Double.parseDouble(data.get("amount").toString()));
            payment.setCurrency((String) data.get("currency"));

            if (data.get("acknowledgedDateMs") != null) {
                Long ts = ((Number) data.get("acknowledgedDateMs")).longValue();
                LocalDate date = Instant.ofEpochMilli(ts).atZone(ZoneId.systemDefault()).toLocalDate();
                payment.setPaymentDate(date);
            } else {
                payment.setPaymentDate(LocalDate.now());
            }

            return paymentRepository.save(payment);

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Bakong token expired or invalid", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify payment", e);
        }
    }
}