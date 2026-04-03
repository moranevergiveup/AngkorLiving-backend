
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