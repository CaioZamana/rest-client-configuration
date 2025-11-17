package com.client.Rest.configuration;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class LoggingInterceptorConfig implements RestClientCustomizer {
    @Override
    public void customize(RestClient.Builder builder) {
        builder.requestInterceptor(new ErrorLoggingInterceptor());
        builder.requestInterceptor(new FullLoggingInterceptor());
    }
}
