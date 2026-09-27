package com.wcc.auth.controller;

import com.wcc.auth.dto.ClientTokenRequestDto;
import com.wcc.auth.dto.CreatePrincipalRequestDto;
import com.wcc.auth.dto.CreatePrincipalResponseDto;
import com.wcc.auth.dto.LoginRequestDto;
import com.wcc.auth.dto.TokenResponseDto;
import com.wcc.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token/client")
    public TokenResponseDto issueClientToken(@Valid @RequestBody ClientTokenRequestDto request) {
        return authService.issueClientToken(request.clientId(), request.clientSecret());
    }

    @PostMapping("/login")
    @PreAuthorize("hasAuthority('LOGIN')")
    public TokenResponseDto loginUser(@Valid @RequestBody LoginRequestDto request) {
        return authService.loginUser(request.username(), request.password());
    }

    @PostMapping("/principal")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CREATE_PRINCIPAL')")
    public CreatePrincipalResponseDto createPrincipal(@Valid @RequestBody CreatePrincipalRequestDto request) {
        return authService.createPrincipal(request.username(), request.password());
    }

    @DeleteMapping("/principal/{principalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('DELETE_PRINCIPAL')")
    public void deletePrincipal(@PathVariable String principalId) {
        authService.deletePrincipal(principalId);
    }
}
