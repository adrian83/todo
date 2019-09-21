package com.github.adrian83.todo.configuration

import org.springframework.stereotype.Component
import org.springframework.web.server.WebFilter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import org.springframework.beans.factory.annotation.Value

@Component
class CorsFilter : WebFilter {
	
	@Value("\${cors.frontend.origin}")
	var frontendOrigin: String = ""
	
    override fun filter(ctx: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
	
		ctx.response.headers.add("Access-Control-Allow-Origin", frontendOrigin)
        ctx.response.headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS")
        ctx.response.headers.add("Access-Control-Allow-Headers", "authorization,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")
        ctx.response.headers.add("Access-Control-Expose-Headers", "authorization,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range")
		
		return chain.filter(ctx)
	}
		
}