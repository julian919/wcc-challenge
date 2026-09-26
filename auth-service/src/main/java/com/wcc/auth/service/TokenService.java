package com.wcc.auth.service;

import com.wcc.auth.dto.TokenResponseDto;
import com.wcc.auth.model.Principal;
import com.wcc.auth.model.Role;
import com.wcc.auth.repository.RoleRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final RoleRepository roleRepository;
    private final Duration tokenTtl;

    public TokenService(JwtEncoder jwtEncoder, RoleRepository roleRepository,
                        @Value("${app.jwt.token-ttl}") Duration tokenTtl) {
        this.jwtEncoder = jwtEncoder;
        this.roleRepository = roleRepository;
        this.tokenTtl = tokenTtl;
    }

    public TokenResponseDto issueToken(Principal principal) {
        Instant now = Instant.now();
        List<String> permissions = roleRepository.findByNameIn(principal.roles()).stream()
                .map(Role::permissions)
                .flatMap(List::stream)
                .distinct()
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("wcc-auth-service")
                .subject(principal.id())
                .issuedAt(now)
                .expiresAt(now.plus(tokenTtl))
                .claim("type", principal.type().name())
                .claim("roles", principal.roles())
                .claim("permissions", permissions)
                .build();
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenResponseDto(accessToken, "Bearer", tokenTtl.toSeconds());
    }
}
