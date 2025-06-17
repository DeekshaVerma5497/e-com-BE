package com.kalavastra.api.config;

import com.kalavastra.api.security.CustomUserDetailsService;
import com.kalavastra.api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService uds;
	private final JwtAuthFilter jwtFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// CORS
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// no CSRF
				.csrf(AbstractHttpConfigurer::disable)

				// public endpoints + preflight
				.authorizeHttpRequests(auth -> auth
						// 1) allow all OPTIONS requests (CORS preflight)
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .requestMatchers("/api/v1/cart/items").permitAll()
						// 2) your existing public endpoints
						.requestMatchers("/api/v1/auth/**", "/api/v1/products/**", "/api/v1/categories/**",
								"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
						.permitAll()
						// 3) everything else (including /api/v1/cart/**) must be authenticated
						.anyRequest().authenticated())

				// stateless session
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// DAO auth provider
				.authenticationProvider(daoAuthenticationProvider())

				// JWT filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(uds);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
		auth.authenticationProvider(daoAuthenticationProvider());
		return auth.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Defines CORS policy and registers it for all paths. This runs before Spring
	 * Security filters and preserves the Authorization header for your JWT filter.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:4200"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
		src.registerCorsConfiguration("/**", config);
		return src;
	}

}
