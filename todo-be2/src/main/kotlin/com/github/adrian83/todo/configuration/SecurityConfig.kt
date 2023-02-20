package com.github.adrian83.todo.configuration

import com.github.adrian83.todo.security.AuthenticationManager
import com.github.adrian83.todo.security.TodoSecurityContextRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securitygWebFilterChain(
        http: ServerHttpSecurity,
        authenticationManager: AuthenticationManager,
        securityContextRepository: TodoSecurityContextRepository
    ): SecurityWebFilterChain {
        return http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS, "/api/v1/**").permitAll()
            .pathMatchers("/api/v1/auth/**").permitAll()
            .pathMatchers("/api/v1/**").authenticated()
            .anyExchange().authenticated()
            .and()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .build()
    }
}
