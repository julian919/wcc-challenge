package com.wcc.users.user.controller;

import com.wcc.users.user.dto.RegisterUserRequestDto;
import com.wcc.users.user.dto.UserDto;
import com.wcc.users.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('REGISTER')")
    public UserDto registerUser(@Valid @RequestBody RegisterUserRequestDto request) {
        return userService.registerUser(request.username(), request.password(), request.firstName(), request.lastName());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('READ_OWN_PROFILE')")
    public UserDto getOwnProfile(@AuthenticationPrincipal Jwt jwt) {
        return userService.getOwnProfile(jwt.getSubject());
    }
}
