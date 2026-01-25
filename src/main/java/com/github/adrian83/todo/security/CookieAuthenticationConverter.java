package com.github.adrian83.todo.security;

import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CookieAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String ACCESS_TOKEN_COOKIE = "accessToken";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst(ACCESS_TOKEN_COOKIE);
        
        if (accessTokenCookie == null || accessTokenCookie.getValue().isBlank()) {
            return Mono.empty();
        }

        return Mono.just(new TokenAuthentication(accessTokenCookie.getValue()));
    }
}
