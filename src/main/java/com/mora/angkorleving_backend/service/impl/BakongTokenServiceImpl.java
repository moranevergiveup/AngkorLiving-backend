//////package com.mora.angkorleving_backend.service;
//////
//////import com.fasterxml.jackson.databind.JsonNode;
////////import com.fasterxml.jackson.databind.ObjectMapper;
////////import com.fasterxml.jackson.databind.ObjectMapper;
//////import com.fasterxml.jackson.databind.ObjectMapper;
//////import lombok.RequiredArgsConstructor;
//////import lombok.extern.slf4j.Slf4j;
//////import org.springframework.beans.factory.annotation.Value;
//////import org.springframework.http.*;
//////import org.springframework.stereotype.Service;
//////import org.springframework.web.client.RestTemplate;
////////import org.springframework.web.client.RestTemplate;
//////
//////import java.time.Instant;
//////import java.util.Map;
//////
//////@Service
//////@RequiredArgsConstructor
//////@Slf4j
//////public class BakongTokenServiceImpl implements BakongTokenService {
//////
////////    private final RestTemplate restTemplate;
////////    private final ObjectMapper mapper;
//////    RestTemplate restTemplate = new RestTemplate();
//////    ObjectMapper mapper = new  ObjectMapper();
//////
//////    @Value("${bakong.base-url}")
//////    private String baseUrl;
//////    @Value("${bakong.email}")
//////    private String email;
//////
//////    private String cachedToken;
//////    private Instant tokenExpiry;
//////
//////    @Override
//////    public synchronized String getToken() {
//////        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
//////            log.info("Using cached token");
//////            return cachedToken;
//////        }
//////
//////        log.info("Renewing token from Bakong");
//////
//////        HttpHeaders headers = new HttpHeaders();
//////        headers.setContentType(MediaType.APPLICATION_JSON);
//////        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("email", email), headers);
//////
//////        ResponseEntity<String> response = restTemplate.exchange(
//////                baseUrl.replaceAll("/+$", "") + "/v1/renew_token",
//////                HttpMethod.POST,
//////                entity,
//////                String.class
//////        );
//////
//////        try {
//////            JsonNode root = mapper.readTree(response.getBody());
//////            JsonNode tokenNode = root.path("data").path("token");
//////            if (tokenNode.isMissingNode() || tokenNode.isNull()) {
//////                throw new RuntimeException("Bakong token not returned");
//////            }
//////
//////            cachedToken = tokenNode.asText();
//////
//////            // Decode JWT to get real expiry
//////            String[] parts = cachedToken.split("\\.");
//////            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
//////            JsonNode payloadNode = mapper.readTree(payload);
//////            long exp = payloadNode.path("exp").asLong();
//////            tokenExpiry = Instant.ofEpochSecond(exp);
//////
//////            log.info("Obtained new token, expires at {}", tokenExpiry);
//////
//////            return cachedToken;
//////        } catch (Exception e) {
//////            throw new RuntimeException("Failed to obtain Bakong token", e);
//////        }
//////    }
//////}
////package com.mora.angkorleving_backend.service;
////
////import com.fasterxml.jackson.databind.JsonNode;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.http.*;
////import org.springframework.stereotype.Service;
////import org.springframework.web.client.RestTemplate;
////
////import java.time.Instant;
////import java.util.Map;
////
////@Service
////@Slf4j
////public class BakongTokenServiceImpl implements BakongTokenService {
////
////    private final RestTemplate restTemplate = new RestTemplate();
////    private final ObjectMapper mapper = new ObjectMapper();
////
////    @Value("${bakong.base-url}")
////    private String baseUrl;
////
////    @Value("${bakong.account-id}")
////    private String bakongAccountId;
////
////    private String cachedToken;
////    private Instant tokenExpiry;
////
////    @Override
////    public synchronized String getToken() {
////        // reuse token if still valid
////        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
////            log.info("Using cached token");
////            return cachedToken;
////        }
////
////        log.info("Renewing token from Bakong");
////
////        // Prepare request
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////
////        HttpEntity<Map<String, String>> request = new HttpEntity<>(
////                Map.of("account_id", bakongAccountId),
////                headers
////        );
////
////        ResponseEntity<String> response = restTemplate.exchange(
////                baseUrl.replaceAll("/+$", "") + "/v1/renew_token",
////                HttpMethod.POST,
////                request,
////                String.class
////        );
////
////        try {
////            JsonNode root = mapper.readTree(response.getBody());
////            JsonNode tokenNode = root.path("data").path("token");
////
////            if (tokenNode.isMissingNode() || tokenNode.isNull()) {
////                throw new RuntimeException("Bakong token not returned");
////            }
////
////            cachedToken = tokenNode.asText();
////
////            // Decode JWT to get expiry
////            String[] parts = cachedToken.split("\\.");
////            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
////            JsonNode payloadNode = mapper.readTree(payload);
////            long exp = payloadNode.path("exp").asLong();
////            tokenExpiry = Instant.ofEpochSecond(exp);
////
////            log.info("Obtained new token, expires at {}", tokenExpiry);
////
////            return cachedToken;
////        } catch (Exception e) {
////            throw new RuntimeException("Failed to obtain Bakong token", e);
////        }
////    }
////}
//package com.mora.angkorleving_backend.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.Instant;
//import java.util.Map;
//
//@Service
//@Slf4j
//public class BakongTokenServiceImpl implements BakongTokenService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Value("${bakong.base-url}")
//    private String baseUrl;
//
//    @Value("${bakong.account-id}")
//    private String bakongAccountId;
//
//    private String cachedToken;
//    private Instant tokenExpiry;
//
//    @Override
//    public synchronized String getToken() {
//        // reuse token if still valid
//        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
//            log.info("Using cached Bakong token");
//            return cachedToken;
//        }
//
//        log.info("Requesting new Bakong token for account: {}", bakongAccountId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> payload = Map.of("bakong_account_id", bakongAccountId);
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    baseUrl.replaceAll("/+$", "") + "/v1/renew_token",
//                    HttpMethod.POST,
//                    request,
//                    String.class
//            );
//
//            // log full response for debugging
//            log.info("Bakong token response: {}", response.getBody());
//
//            if (!response.getStatusCode().is2xxSuccessful()) {
//                throw new RuntimeException("Bakong returned HTTP " + response.getStatusCode());
//            }
//
//            JsonNode root = mapper.readTree(response.getBody());
//            JsonNode tokenNode = root.path("data").path("token");
//            if (tokenNode.isMissingNode() || tokenNode.isNull()) {
//                throw new RuntimeException("Bakong token not returned in response");
//            }
//
//            cachedToken = tokenNode.asText();
//
//            // decode JWT to get expiry
//            try {
//                String[] parts = cachedToken.split("\\.");
//                if (parts.length < 2) {
//                    throw new RuntimeException("Invalid JWT token format");
//                }
//                String payloadPart = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
//                JsonNode payloadNode = mapper.readTree(payloadPart);
//                long exp = payloadNode.path("exp").asLong();
//                tokenExpiry = Instant.ofEpochSecond(exp);
//                log.info("Obtained new token, expires at {}", tokenExpiry);
//            } catch (Exception e) {
//                log.warn("Failed to parse JWT expiry, using default 5 minutes", e);
//                tokenExpiry = Instant.now().plusSeconds(300);
//            }
//
//            return cachedToken;
//
//        } catch (HttpClientErrorException e) {
//            log.error("HTTP error while obtaining Bakong token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
//            throw new RuntimeException("Failed to obtain Bakong token due to HTTP error", e);
//        } catch (Exception e) {
//            log.error("Error while obtaining Bakong token", e);
//            throw new RuntimeException("Failed to obtain Bakong token", e);
//        }
//    }
//}

package com.mora.angkorleving_backend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mora.angkorleving_backend.service.BakongTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Service
@Slf4j
public class BakongTokenServiceImpl implements BakongTokenService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${BAKONG.BASE-URL}")
    private String baseUrl;

//    @Value("${BAKONG_ACCOUNT_USERNAME}")
//    private String bakongAccountId;

//    @Value("${bakong.base-url}")
//    private String baseUrl;
    @Value("${BAKONG_EMAIL}")
    private String email;

    private String cachedToken;
    private Instant tokenExpiry;

    @Override
    public synchronized String getToken() {
        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            log.info("Using cached token");
            return cachedToken;
        }

        log.info("Renewing token from Bakong");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("email", email), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl.replaceAll("/+$", "") + "/v1/renew_token",
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode tokenNode = root.path("data").path("token");
            if (tokenNode.isMissingNode() || tokenNode.isNull()) {
                throw new RuntimeException("Bakong token not returned");
            }

            cachedToken = tokenNode.asText();

            // Decode JWT to get real expiry
            String[] parts = cachedToken.split("\\.");
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payloadNode = mapper.readTree(payload);
            long exp = payloadNode.path("exp").asLong();
            tokenExpiry = Instant.ofEpochSecond(exp);

            log.info("Obtained new token, expires at {}", tokenExpiry);

            return cachedToken;
        } catch (Exception e) {
            throw new RuntimeException("Failed to obtain Bakong token", e);
        }
    }
}