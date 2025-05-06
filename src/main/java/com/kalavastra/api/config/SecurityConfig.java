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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService uds;
    private final JwtAuthFilter            jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          // 1) no CSRF (we’re stateless)
          .csrf(AbstractHttpConfigurer::disable)

          // 2) public endpoints
          .authorizeHttpRequests(auth -> auth
            .requestMatchers(
              "/api/v1/auth/**",
              "/swagger-ui.html",
              "/swagger-ui/**",
              "/v3/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
          )

          // 3) stateless session
          .sessionManagement(sm -> sm
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )

          // 4) inject our DaoAuthProvider
          .authenticationProvider(daoAuthenticationProvider())

          // 5) plug in the JWT filter
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

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

    /** exposed so your AuthController (or service) can perform the actual `authenticate(...)` call */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = 
          http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.authenticationProvider(daoAuthenticationProvider());
        return auth.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
