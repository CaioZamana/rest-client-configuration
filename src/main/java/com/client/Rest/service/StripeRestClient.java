package com.client.Rest.service;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class StripeRestClient {

    private final RestClient stripeClient;

    public StripeRestClient(RestClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    // Criar pagamento
    public ResponseEntity<String> createPaymentIntent(Integer amount, String currency) {
        return stripeClient.post()
                .uri("/payment_intents")
                .body("amount=" + amount + "&currency=" + currency)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(String.class);
    }

    // Buscar pagamento
    public ResponseEntity<String> getPaymentIntent(String id) {
        return stripeClient.get()
                .uri("/payment_intents/{id}", id)
                .retrieve()
                .toEntity(String.class);
    }
}
