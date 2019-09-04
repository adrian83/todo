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

@Component
class JwtTokenEncoder {
	
	companion object {
        const val SECRET = "niufr92hrfu2h3rhweuf9234yhfuio2"
		val UTC_DATETIME_OFFSET = OffsetDateTime.now(ZoneOffset.UTC)
		val SYGNATURE_ALGORITHM = SignatureAlgorithm.HS256
		
		const val USER_ID = "userID"
		const val USER_EMAIL = "email"
    }
	
	
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
			.signWith(SYGNATURE_ALGORITHM, SECRET.toByteArray())
			.compact();
	}
	
	fun tokenFromString(tokenAsStr: String): AuthToken {
		
		var key = SecretKeySpec(SECRET.toByteArray(), SYGNATURE_ALGORITHM.getValue())
		var jwtClaims = Jwts.parser().setSigningKey(key).parseClaimsJws(tokenAsStr)
		var claims = jwtClaims.getBody()
		
		var userIdStr = claims.get(USER_ID).toString()
		var userEmail = claims.get(USER_EMAIL).toString()
		
		return AuthToken(userIdStr.toLong(), userEmail)
	}
	
}