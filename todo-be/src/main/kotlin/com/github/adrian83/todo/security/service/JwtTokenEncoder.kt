package com.github.adrian83.todo.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.ZoneOffset
import java.time.OffsetDateTime
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import org.springframework.beans.factory.annotation.Value

@Component
class JwtTokenEncoder {
	
	companion object {
		val SYGNATURE_ALGORITHM = SignatureAlgorithm.HS256
		
		const val USER_ID = "userID"
		const val USER_EMAIL = "email"
    }
	
	@Value("\${security.sygnature.secret}")
	var secret: String = ""
	
	
	fun tokenToString(authToken: AuthToken): String {
		
		val validUntil = Date.from(LocalDate.now()
				.plusDays(5)
				.atStartOfDay()
				.atZone(ZoneId.systemDefault())
			.toInstant());
		 
		return Jwts.builder()
			.setSubject("users")
			.setExpiration(validUntil)
			.claim(USER_ID, authToken.userId)
			.claim(USER_EMAIL, authToken.email)
			.signWith(SYGNATURE_ALGORITHM, secret.toByteArray())
			.compact();
	}
	
	fun tokenFromString(tokenAsStr: String): AuthToken {
		
		var key = SecretKeySpec(secret.toByteArray(), SYGNATURE_ALGORITHM.getValue())
		var claims = Jwts.parser()
			.setSigningKey(key)
			.parseClaimsJws(tokenAsStr)
			.getBody()
		
		var userId = claims.get(USER_ID).toString().toLong()
		var userEmail = claims.get(USER_EMAIL).toString()
		
		return AuthToken(userId, userEmail)
	}
	
}