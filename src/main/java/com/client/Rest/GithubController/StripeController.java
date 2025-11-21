package com.client.Rest.GithubController;

import com.client.Rest.service.StripeRestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeRestClient stripeService;

    public StripeController(StripeRestClient stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(
            @RequestParam Integer amount,
            @RequestParam(defaultValue = "usd") String currency
    ) {
        return stripeService.createPaymentIntent(amount, currency);
    }

    @GetMapping("/payment-intent/{id}")
    public ResponseEntity<String> getPaymentIntent(@PathVariable String id) {
        return stripeService.getPaymentIntent(id);
    }
}
