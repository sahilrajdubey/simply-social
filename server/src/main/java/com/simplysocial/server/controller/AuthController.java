package com.simplysocial.server.controller;

import com.simplysocial.server.domain.User;
import com.simplysocial.server.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Android calls this after successful supabase login
    @PostMapping("/sync")
    public User syncUser(@AuthenticationPrincipal Jwt jwt) {
        // Trust only the token, never user input
        String userId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        if (email == null) {
            throw new IllegalStateException("Token does not contain email claim");
        }
        return authService.syncUser(userId, email);
    }
}