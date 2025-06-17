package com.kalavastra.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;

	public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getMethod() + " " + request.getRequestURI();
		log.debug("[JWT] Incoming request: {}", path);

		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			try {
				// this both parses and validates signature + expiration
				String username = jwtUtil.getUsernameFromToken(token);
				log.debug("[JWT] Token OK, subject = {}", username);

				// only set auth if not already present
				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					var userDetails = userDetailsService.loadUserByUsername(username);
					var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);
					log.debug("[JWT] Authenticated user: {}", username);
				}

			} catch (JwtException | IllegalArgumentException e) {
				// catch *all* JJWT parsing/validation errors
				log.warn("[JWT] Token processing failed: {}", e.getMessage());
			}
		} else {
			log.debug("[JWT] No Bearer header, skipping JWT logic");
		}

		filterChain.doFilter(request, response);
	}
}
