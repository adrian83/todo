package com.github.adrian83.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final TokenAuthenticationManager tokenAuthenticationManager;

    public SecurityConfiguration(TokenAuthenticationManager tokenAuthenticationManager) {
        this.tokenAuthenticationManager = tokenAuthenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(tokenAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new CookieAuthenticationConverter());
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/", "/login", "/register").permitAll()
                .pathMatchers("/h2-console/**").permitAll()
                .anyExchange().authenticated()
            )
            .addFilterAfter(authenticationWebFilter, org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((exchange, ex) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                })
            );

        return http.build();
    }
}
