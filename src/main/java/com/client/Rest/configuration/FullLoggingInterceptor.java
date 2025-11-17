package com.client.Rest.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FullLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FullLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {

        long start = System.currentTimeMillis();

        logRequest(request, body);

        ClientHttpResponse response = execution.execute(request, body);

        long latency = System.currentTimeMillis() - start;

        logResponse(request, response, latency);

        return response; // não altera fluxo, só observa
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.info("""
                
                === OUTGOING REQUEST ===
                method={}
                uri={}
                headers={}
                body={}
                """,
                request.getMethod(),
                request.getURI(),
                request.getHeaders(),
                body.length > 0 ? new String(body, StandardCharsets.UTF_8) : "<empty>"
        );
    }

    private void logResponse(HttpRequest request, ClientHttpResponse response, long latencyMs) {
        try {
            String body = readBody(response);

            log.info("""
                    
                    === INCOMING RESPONSE ===
                    method={}
                    uri={}
                    status={}
                    headers={}
                    body={}
                    latencyMs={}
                    """,
                    request.getMethod(),
                    request.getURI(),
                    response.getStatusCode().value(),
                    response.getHeaders(),
                    body,
                    latencyMs
            );
        } catch (Exception e) {
            log.warn("Failed to log response: {}", e.getMessage());
        }
    }

    private String readBody(ClientHttpResponse response) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.isEmpty() ? "<empty>" : sb.toString().trim();
        } catch (Exception e) {
            return "<unavailable>";
        }
    }
}
