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

public class ErrorLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ErrorLoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {

        ClientHttpResponse response = execution.execute(request, body);

        int status = response.getStatusCode().value();

        if (status >= 400) {
            log.error("""
                    
                    === REST CLIENT ERROR ===
                    method={}
                    uri={}
                    status={}
                    headers={}
                    body={}
                    """,
                    request.getMethod(),
                    request.getURI(),
                    status,
                    response.getHeaders(),
                    readBody(response)
            );
        }

        return response; // não lança exceção, só loga
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
