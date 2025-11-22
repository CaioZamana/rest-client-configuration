package com.client.Rest.controller;

import com.client.Rest.service.ViaCepRestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/viacep")
public class ViaCepController {

    private final ViaCepRestClient viaCepService;

    public ViaCepController(ViaCepRestClient viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<String> findCep(@PathVariable String cep) {
        return viaCepService.findCep(cep);
    }
}
