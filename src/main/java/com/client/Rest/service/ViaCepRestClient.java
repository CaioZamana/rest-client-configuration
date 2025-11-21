package com.client.Rest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ViaCepRestClient {

    private final RestClient viaCepClient;

    public ViaCepRestClient(RestClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }


    public ResponseEntity<String> findCep(String cep) {
        return viaCepClient.get()
                .uri("/{cep}/json", cep)
                .retrieve()
                .toEntity(String.class);
    }
}
