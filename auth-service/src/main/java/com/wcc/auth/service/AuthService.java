package com.wcc.auth.service;

import com.wcc.auth.dto.TokenResponseDto;
import com.wcc.auth.exception.InvalidClientException;
import com.wcc.auth.model.Principal;
import com.wcc.auth.repository.PrincipalRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PrincipalRepository principalRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(PrincipalRepository principalRepository, PasswordEncoder passwordEncoder,
            TokenService tokenService) {
        this.principalRepository = principalRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public TokenResponseDto issueClientToken(String clientId, String clientSecret) {
        Principal client = principalRepository.findByTypeAndLogin("CLIENT", "CLIENT_ID", clientId)
                .filter(principal -> passwordEncoder.matches(clientSecret, principal.secretHash()))
                .orElseThrow(InvalidClientException::new);
        return tokenService.issueToken(client);
    }
}
