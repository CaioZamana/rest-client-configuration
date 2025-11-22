package com.client.Rest.configuration;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.function.Consumer;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient stripeClient(
            RestClient.Builder builder,
            @Value("${rest-client.services.stripe.url}") String url,
            @Value("${rest-client.services.stripe.connection-time-out}") int connectTimeout,
            @Value("${rest-client.services.stripe.read-time-out}") int readTimeout
    ) {
        Consumer<RequestHeadersSpec<?>> defaultHeaders = req -> {
            req.header("Authorization", "Bearer sk_test_123456789");
            req.header("Stripe-Version", "2023-10-16");
        };

        return buildClient(builder, url, connectTimeout, readTimeout, defaultHeaders);
    }

    @Bean
    public RestClient githubClient(
            RestClient.Builder builder,
            @Value("${rest-client.services.github.url}") String url,
            @Value("${rest-client.services.github.connection-time-out}") int connectTimeout,
            @Value("${rest-client.services.github.read-time-out}") int readTimeout
    ) {
        Consumer<RequestHeadersSpec<?>> defaultHeaders = req -> {
            req.header("Accept", "application/vnd.github+json");
            req.header("X-GitHub-Api-Version", "2022-11-28");
        };

        return buildClient(builder, url, connectTimeout, readTimeout, defaultHeaders);
    }

    @Bean
    public RestClient viaCepClient(
            RestClient.Builder builder,
            @Value("${rest-client.services.viacep.url}") String url,
            @Value("${rest-client.services.viacep.connection-time-out}") int connectTimeout,
            @Value("${rest-client.services.viacep.read-time-out}") int readTimeout
    ) {
        // Não precisa de headers
        return buildClient(builder, url, connectTimeout, readTimeout, null);
    }


    /**
     * Cria e configura um RestClient dedicado para um serviço externo.
     * Usamos timeouts explícitos para evitar chamadas presas em espera infinita,
     * o que protege o pool de threads e evita cascata de falhas na aplicação.
     */
    private RestClient buildClient(
            RestClient.Builder builder,
            String url,
            int connectTimeout,
            int readTimeout,
            @Nullable Consumer<RequestHeadersSpec<?>> defaultHeaders
    ) {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();

        JdkClientHttpRequestFactory jdkFactory = new JdkClientHttpRequestFactory(httpClient);
        jdkFactory.setReadTimeout(Duration.ofMillis(readTimeout));

        ClientHttpRequestFactory bufferingFactory =
                new BufferingClientHttpRequestFactory(jdkFactory);

        return builder
                .clone()
                .baseUrl(url)
                .requestFactory(bufferingFactory)   // <- funciona 100% com interceptores
                .defaultRequest(defaultHeaders)
                .requestInterceptor(new ErrorLoggingInterceptor())
                .build();
    }
}
