package com.wcc.auth.controller;

import com.wcc.auth.dto.ClientTokenRequestDto;
import com.wcc.auth.dto.TokenResponseDto;
import com.wcc.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
