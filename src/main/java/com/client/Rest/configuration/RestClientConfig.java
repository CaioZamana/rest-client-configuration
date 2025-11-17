package com.client.Rest.configuration;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.function.Consumer;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient apiExample1Client(
            RestClient.Builder builder,
            @Value("${rest-client.services.api-example-1.url}") String url,
            @Value("${rest-client.services.api-example-1.connection-time-out}") int connectTimeout,
            @Value("${rest-client.services.api-example-1.read-time-out}") int readTimeout
    ) {

        // Headers globais usados para identificar o cliente na API destino
        // Isso permite rastreamento, auditoria e debugging entre serviços.
        Consumer<RequestHeadersSpec<?>> defaultHeaders = req -> {
            req.header("X-Client", "ApiExample1");
            req.header("Accept", "application/json");
            req.header("X-Version", "1.0.0");
        };

        return buildClient(builder, url, connectTimeout, readTimeout, defaultHeaders);
    }


    @Bean
    public RestClient apiExample2Client(
            RestClient.Builder builder,
            @Value("${rest-client.services.api-example-2.url}") String url,
            @Value("${rest-client.services.api-example-2.connection-time-out}") int connectTimeout,
            @Value("${rest-client.services.api-example-2.read-time-out}") int readTimeout
    ) {

        // Cada client pode possuir seus headers específicos. Isso evita
        // conflitos entre serviços diferentes e facilita a observabilidade.
        Consumer<RequestHeadersSpec<?>> defaultHeaders = req -> {
            req.header("X-Client", "ApiExample2");
            req.header("Accept", "application/json");
            req.header("X-Version", "2.0.0");
        };

        return buildClient(builder, url, connectTimeout, readTimeout, defaultHeaders);
    }

    @Bean
    public RestClient apiExample3Client(
            RestClient.Builder builder,
            @Value("${rest-client.services.api-example-3.url}") String url,
            @Value("${rest-client.services.api-example-3.connection-time-out}") int connectTimeout,
            @Value("${rest-client.services.api-example-3.read-time-out}") int readTimeout
    ) {
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

        // Timeout de conexão impede que a aplicação fique bloqueada
        // quando o host remoto está indisponível ou lento para responder o handshake TCP.
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();

        // Timeout de leitura evita travamento da thread em casos onde
        // a API destino aceita a conexão mas nunca envia resposta.
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofMillis(readTimeout));

//Old
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setConnectTimeout(connectTimeout);
//        factory.setReadTimeout(readTimeout);

        return builder
                .clone()
                .baseUrl(url)                 // Força o client a sempre apontar para a URL do serviço destino
                .requestFactory(factory)      // Garante controle total de timeouts e comportamento baixo nível
                .defaultRequest(defaultHeaders) // Injeta headers padrão em todas as requisições
                .build();
    }
}
