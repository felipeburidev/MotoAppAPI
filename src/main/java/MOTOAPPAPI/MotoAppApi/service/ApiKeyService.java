package MOTOAPPAPI.MotoAppApi.service;

import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {

    private final String validApiKey = "yS3nqf99s0p2I9ZbBRXEZl8JO4tcpzb3XynDC3AwGuNLCOJeJQ";

    public boolean isValidApiKey(String apiKey) {
        return validApiKey.equals(apiKey);
    }
}