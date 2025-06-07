package com.kalavastra.api.config;

import com.kalavastra.api.security.CustomUserDetailsService;
import com.kalavastra.api.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService uds;
	private final JwtAuthFilter jwtFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// 1) no CSRF (we’re stateless)
				.csrf(AbstractHttpConfigurer::disable)

				// 2) register CORS filter FIRST, so that preflight (OPTIONS) requests get the
				// right headers
				.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)

				// 3) public endpoints
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/v1/auth/**", "/api/v1/products/**", "/api/v1/categories/**",
								"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
						.permitAll().anyRequest().authenticated())

				// 4) stateless session
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 5) inject our DaoAuthProvider
				.authenticationProvider(daoAuthenticationProvider())

				// 6) plug in the JWT filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/** so that Spring Security knows how to load users + check passwords */
	@Bean
	public AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(uds);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/**
	 * exposed so your AuthController (or service) can perform the actual
	 * `authenticate(...)` call
	 */
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

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}
}
