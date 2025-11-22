package com.client.Rest.controller;

import com.client.Rest.service.GithubRestClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubRestClient githubService;

    public GithubController(GithubRestClient githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<String> getUser(@PathVariable String username) {
        return githubService.getUser(username);
    }

    @GetMapping("/repos/{username}")
    public ResponseEntity<String> listRepos(@PathVariable String username) {
        return githubService.listRepos(username);
    }
}
