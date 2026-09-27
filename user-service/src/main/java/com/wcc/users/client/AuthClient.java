package com.wcc.users.client;

import com.wcc.users.user.exception.UsernameTakenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient restClient;
    private final ServiceTokenProvider serviceTokenProvider;

    public AuthClient(RestClient.Builder restClientBuilder, @Value("${app.auth.base-url}") String authBaseUrl,
                      ServiceTokenProvider serviceTokenProvider) {
        this.restClient = restClientBuilder.baseUrl(authBaseUrl).build();
        this.serviceTokenProvider = serviceTokenProvider;
    }

    public String createPrincipal(String username, String password) {
        return restClient.post()
                .uri("/api/auth/principal")
                .headers(headers -> headers.setBearerAuth(serviceTokenProvider.getToken()))
                .body(new CreatePrincipalRequest(username, password))
                .retrieve()
                .onStatus(status -> status.isSameCodeAs(HttpStatus.CONFLICT), (request, response) -> {
                    throw new UsernameTakenException(username);
                })
                .body(CreatePrincipalResponse.class)
                .id();
    }

    public void deletePrincipal(String principalId) {
        restClient.delete()
                .uri("/api/auth/principal/{principalId}", principalId)
                .headers(headers -> headers.setBearerAuth(serviceTokenProvider.getToken()))
                .retrieve()
                .toBodilessEntity();
    }

    record CreatePrincipalRequest(String username, String password) {
    }

    record CreatePrincipalResponse(String id) {
    }
}
