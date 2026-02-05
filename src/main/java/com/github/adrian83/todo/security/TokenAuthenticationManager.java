package com.github.adrian83.todo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.github.adrian83.todo.service.TokenService;
import com.github.adrian83.todo.service.exception.TokenValidationException;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class TokenAuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationManager.class);

    private final TokenService tokenService;

    public TokenAuthenticationManager(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (!(authentication instanceof TokenAuthentication tokenAuth)) {
            return Mono.empty();
        }

        return Mono.fromCallable(() -> {
            try {
                Claims claims = tokenService.validateAccessToken(tokenAuth.getToken());
                String username = claims.get("username", String.class);
                Long userId = claims.get("userId", Long.class);
                
                UserPrincipal principal = new UserPrincipal(userId, username);
                tokenAuth.setPrincipal(principal);
                tokenAuth.setAuthenticated(true);
                
                return (Authentication) tokenAuth;
            } catch (TokenValidationException e) {
                return null;
            }
        })
        .filter(auth -> auth != null)
        .switchIfEmpty(Mono.empty());
    }
}
