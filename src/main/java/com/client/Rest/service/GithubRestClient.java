package com.client.Rest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GithubRestClient {

    private final RestClient githubClient;

    public GithubRestClient(RestClient githubClient) {
        this.githubClient = githubClient;
    }

    public ResponseEntity<String> getUser(String username) {
        return githubClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .toEntity(String.class);
    }

    public ResponseEntity<String> listRepos(String username) {
        return githubClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .toEntity(String.class);
    }
}
