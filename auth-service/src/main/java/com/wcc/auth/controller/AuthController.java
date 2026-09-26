package com.wcc.auth.controller;

import com.wcc.auth.dto.ClientTokenRequestDto;
import com.wcc.auth.dto.RegisterResponseDto;
import com.wcc.auth.dto.TokenResponseDto;
import com.wcc.auth.dto.RegisterRequestDto;
import com.wcc.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('REGISTER')")
    public RegisterResponseDto registerUser(@Valid @RequestBody RegisterRequestDto request) {
        return authService.registerUser(request.username(), request.password());
    }

    @PostMapping("/login")
    @PreAuthorize("hasAuthority('LOGIN')")
    public TokenResponseDto loginUser(@Valid @RequestBody RegisterRequestDto request) {
        return authService.loginUser(request.username(), request.password());
    }
}
