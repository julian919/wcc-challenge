package com.wcc.users.client;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ServiceTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(ServiceTokenProvider.class);
    private static final Duration REFRESH_BEFORE_EXPIRY = Duration.ofMinutes(1);

    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;
    private final ReentrantLock lock = new ReentrantLock();
    private String token;
    private Instant expiresAt = Instant.EPOCH;

    public ServiceTokenProvider(RestClient.Builder restClientBuilder,
                                @Value("${app.auth.base-url}") String authBaseUrl,
                                @Value("${app.auth.client-id}") String clientId,
                                @Value("${app.auth.client-secret}") String clientSecret) {
        this.restClient = restClientBuilder.baseUrl(authBaseUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getToken() {
        lock.lock();
        try {
            if (Instant.now().isAfter(expiresAt.minus(REFRESH_BEFORE_EXPIRY))) {
                TokenResponse response = restClient.post()
                        .uri("/api/auth/token/client")
                        .body(new ClientTokenRequest(clientId, clientSecret))
                        .retrieve()
                        .body(TokenResponse.class);
                token = response.accessToken();
                expiresAt = Instant.now().plusSeconds(response.expiresIn());
                log.info("Fetched a new service token");
            }
            return token;
        } finally {
            lock.unlock();
        }
    }

    record ClientTokenRequest(String clientId, String clientSecret) {
    }

    record TokenResponse(String accessToken, long expiresIn) {
    }
}
