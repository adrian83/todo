package com.github.adrian83.todo.security

import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import org.springframework.security.core.context.SecurityContext
import reactor.core.publisher.Mono
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import com.github.adrian83.todo.domain.user.model.User

@Component
class TodoSecurityContextRepository(var authenticationManager: TodoAuthenticationManager): ServerSecurityContextRepository{
	
	override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void>? {
		return Mono.empty();
	}

	override fun load(exchange: ServerWebExchange?): Mono<SecurityContext>? {
		
		System.out.println("loading security context")
		
		if(exchange == null) {
			return  Mono.empty();
		}
		
		System.out.println("loading security context 1")
		
		var request = exchange.getRequest();
		var authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader != null) {
			System.out.println("loading security context 3")
			var authToken = authHeader;
			var auth = UsernamePasswordAuthenticationToken(authToken, authToken);
			var authMono = this.authenticationManager.authenticate(auth)
			if(authMono == null){
				System.out.println("loading security context 4")
				return  Mono.empty();
			}
			
			var user = User("adr@om.po")
			var userData = TodoUserDetails(user)
			var auth2 = UsernamePasswordAuthenticationToken(userData, null, userData.getAuthorities())
			System.out.println("loading security context 5" + auth.isAuthenticated())
			return Mono.just(SecurityContextImpl(auth2))
	

		} else {
			System.out.println("loading security context 6")
			return Mono.empty();
		}
		
		
	}
}