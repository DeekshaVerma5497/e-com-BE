package com.kalavastra.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	// injected from application.yml
	@Value("${app.jwt.secret}")
	private String jwtSecretString;

	@Value("${app.jwt.expirationMs}")
	private long jwtExpirationMs;

	// will be created from the raw secret string
	private Key jwtSecretKey;

	@PostConstruct
	public void init() {
		// if your secret is plain UTF‑8, use this:
		jwtSecretKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
		//
		// If you actually store it base64‑encoded, you can decode first:
		// byte[] decoded = Decoders.BASE64.decode(jwtSecretString);
		// jwtSecretKey = Keys.hmacShaKeyFor(decoded);
	}

	public String generateToken(String username) {
		Date now = new Date();
		Date expires = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expires)
				.signWith(jwtSecretKey, SignatureAlgorithm.HS256).compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			// invalid, expired, etc.
			return false;
		}
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
