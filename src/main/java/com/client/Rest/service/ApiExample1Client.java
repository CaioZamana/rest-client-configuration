package com.client.Rest.service;

import com.client.Rest.dto.request.CreateOrderRequest;
import com.client.Rest.dto.request.CreateUserRequest;
import com.client.Rest.dto.request.UpdateUserRequest;
import com.client.Rest.dto.response.OrderResponse;
import com.client.Rest.dto.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ApiExample1Client {

    private static final Logger log = LoggerFactory.getLogger(ApiExample1Client.class);


    private final RestClient api;

    public ApiExample1Client(RestClient apiExample1Client) {
        this.api = apiExample1Client;
    }

    // ==========================================================
    // 1) GET simples
    // ==========================================================
    public ResponseEntity<List<UserResponse>> getUsers() {
        return api.get()
                .uri("/users")
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=getUsers httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(new ParameterizedTypeReference<List<UserResponse>>() {});
    }

    // ==========================================================
    // 2) GET com path variable
    // ==========================================================
    public ResponseEntity<UserResponse> getUserById(Long id) {
        return api.get()
                .uri("/users/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=getUserById httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(UserResponse.class);
    }

    // ==========================================================
    // 3) GET com query params
    // ==========================================================
    public ResponseEntity<List<UserResponse>> searchUsers(String name, boolean active) {
        return api.get()
                .uri(uri -> uri
                        .path("/users/search")
                        .queryParam("name", name)
                        .queryParam("active", active)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=searchUsers httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(new ParameterizedTypeReference<>() {
                });
    }

    // ==========================================================
    // 4) GET com path + query params
    // ==========================================================
    public ResponseEntity<List<OrderResponse>> getUserOrders(Long id, int page, int size) {
        return api.get()
                .uri(uri -> uri
                        .path("/users/{id}/orders")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(id))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=getUserOrders httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(new ParameterizedTypeReference<>() {
                });
    }

    // ==========================================================
    // 5) POST com body
    // ==========================================================
    public ResponseEntity<UserResponse> createUser(CreateUserRequest request) {
        return api.post()
                .uri("/users")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=createUser httpMethod=POST status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(UserResponse.class);
    }

    // ==========================================================
    // 6) POST com body + query param
    // ==========================================================
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest request, boolean notify) {
        return api.post()
                .uri(uri -> uri
                        .path("/orders")
                        .queryParam("notify", notify)
                        .build())
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=createOrder httpMethod=POST status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(OrderResponse.class);
    }

    // ==========================================================
    // 7) PUT + path + body
    // ==========================================================
    public ResponseEntity<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        return api.put()
                .uri("/users/{id}", id)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=updateUser httpMethod=PUT status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(UserResponse.class);
    }

    // ==========================================================
    // 8) DELETE
    // ==========================================================
    public ResponseEntity<Void> deleteUser(Long id) {
        return api.delete()
                .uri("/users/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=deleteUser httpMethod=DELETE status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })

                .toBodilessEntity();
    }

    // ==========================================================
    // 9) GET com headers fixos
    // ==========================================================
    public ResponseEntity<String> getWithFixedHeaders() {
        return api.get()
                .uri("/secure-data")
                .header("Client-Version", "1.0.0")
                .header("X-Source", "InternalApp")
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=getWithFixedHeaders httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(String.class);
    }

    // ==========================================================
    // 10) GET com header dinâmico
    // ==========================================================
    public ResponseEntity<String> getWithDynamicHeader(String requestId) {
        return api.get()
                .uri("/audit")
                .header("X-Request-ID", requestId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=getWithDynamicHeader httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(String.class);
    }

    // ==========================================================
    // 11) GET com Authorization
    // ==========================================================
    public ResponseEntity<UserResponse> getProfileWithToken(String token) {
        return api.get()
                .uri("/profile")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("event=getProfileWithToken httpMethod=GET status={} body={}",
                            res.getStatusCode().value(),
                            res.getBody());
                })
                .toEntity(UserResponse.class);
    }
}
