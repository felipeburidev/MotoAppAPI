package MOTOAPPAPI.MotoAppApi.service;

import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {

    private final String validApiKey = "yS3nqf99s0p2I9ZbBRXEZl8JO4tcpzb3XynDC3AwGuNLCOJeJQ";

    public boolean isValidApiKey(String apiKey) {

        System.out.println("=== DEBUG API KEY ===");
        System.out.println("API Key recebida: " + apiKey);
        System.out.println("API Key esperada: " + validApiKey);
        System.out.println("São iguais? " + validApiKey.equals(apiKey));

        boolean isValid = validApiKey.equals(apiKey);
        System.out.println("Resultado validação: " + isValid);

        return isValid;
    }}



