package org.stand.springbootproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.stand.springbootproject.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable csrf
                .csrf()
                .disable()
                // Authorize http requests
                .authorizeHttpRequests()
                // Allow unauthenticated access to these URLs (auth)
                .requestMatchers("/api/v1/auth/**")
                .permitAll()
                // Allow unauthenticated access to these URLs (springdoc)
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                .permitAll()
                // Require authentication for all other URLs
                .anyRequest()
                .authenticated()
                .and()
                // Configure session management
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS: new session for each request
                .and()
                // Set authentication provider
                .authenticationProvider(authenticationProvider)
                // Add jwtAuthFilter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
