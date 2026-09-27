package com.wcc.auth.service;

import com.wcc.auth.dto.CreatePrincipalResponseDto;
import com.wcc.auth.dto.TokenResponseDto;
import com.wcc.auth.exception.InvalidClientException;
import com.wcc.auth.exception.InvalidCredentialsException;
import com.wcc.auth.exception.UsernameTakenException;
import com.wcc.auth.model.Principal;
import com.wcc.auth.model.Principal.LoginType;
import com.wcc.auth.model.Principal.PrincipalType;
import com.wcc.auth.repository.PrincipalRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PrincipalRepository principalRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final String dummyPasswordHash;

    public AuthService(PrincipalRepository principalRepository, PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.principalRepository = principalRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.dummyPasswordHash = passwordEncoder.encode("dummy-password-for-unknown-users");
    }

    public TokenResponseDto issueClientToken(String clientId, String clientSecret) {
        Principal client = principalRepository
                .findByTypeAndLogin(PrincipalType.CLIENT, LoginType.CLIENT_ID, clientId)
                .filter(principal -> passwordEncoder.matches(clientSecret, principal.secretHash()))
                .orElseThrow(InvalidClientException::new);
        return tokenService.issueToken(client);
    }

    public CreatePrincipalResponseDto createPrincipal(String username, String password) {
        Principal user = new Principal(null, PrincipalType.USER, passwordEncoder.encode(password),
                List.of("USER"), List.of(new Principal.Login(LoginType.USERNAME, username)));
        try {
            Principal saved = principalRepository.save(user);
            return new CreatePrincipalResponseDto(saved.id());
        } catch (DuplicateKeyException e) {
            throw new UsernameTakenException(username);
        }
    }

    public void deletePrincipal(String principalId) {
        principalRepository.deleteByIdAndType(principalId, PrincipalType.USER);
    }

    public TokenResponseDto loginUser(String username, String password) {
        Optional<Principal> user =
                principalRepository.findByTypeAndLogin(PrincipalType.USER, LoginType.USERNAME, username);
        String hashToCheck = user.map(Principal::secretHash).orElse(dummyPasswordHash);
        boolean passwordMatches = passwordEncoder.matches(password, hashToCheck);
        if (user.isEmpty() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }
        return tokenService.issueToken(user.get());
    }
}
