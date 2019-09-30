package com.github.adrian83.todo.configuration

import org.springframework.stereotype.Component
import org.springframework.web.server.WebFilter
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod

@Component
class CorsFilter : WebFilter {
	
	companion object {
		
		const val ALLOWED_ORIGIN_LABEL = "Access-Control-Allow-Origin"
        const val ALLOWED_HEADERS_LABEL = "Access-Control-Allow-Headers"
		const val EXPOSED_HEADERS_LABEL = "Access-Control-Expose-Headers"
		const val ALLOWED_METHODS_LABEL = "Access-Control-Allow-Methods"
		
		const val ALLOWED_METHODS_VALUE = "GET, PUT, POST, DELETE, OPTIONS"
		const val ALLOWED_HEADERS_VALUE = "Authorization,Content-Type" 
		const val EXPOSED_HEADERS_VALUE = "Authorization,Content-Type" 
    }
	
	@Value("\${cors.frontend.origin}")
	var frontendOrigin: String = ""
	
	
    override fun filter(ctx: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

		ctx.response.headers.add(ALLOWED_ORIGIN_LABEL, frontendOrigin)
	    ctx.response.headers.add(ALLOWED_METHODS_LABEL, ALLOWED_METHODS_VALUE)
	    ctx.response.headers.add(ALLOWED_HEADERS_LABEL, ALLOWED_HEADERS_VALUE)
	    ctx.response.headers.add(EXPOSED_HEADERS_LABEL, EXPOSED_HEADERS_VALUE)
			
		return chain.filter(ctx)
	}
		
}
