package com.example.lab04.config;

import com.example.lab04.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// This new annotation enables @PreAuthorize
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // This is the new JWT Auth Filter you will create in the next step
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Bean for BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean for AuthenticationManager (needed for /login)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Make API stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. Define NEW authorization rules
                .authorizeHttpRequests(auth -> auth
                        // --- Public Endpoints (Always allowed) ---
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        // --- Secure WRITE Endpoints (Roles required) ---
                        .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/grades/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasAnyRole("ADMIN", "INSTRUCTOR")

                        .requestMatchers(HttpMethod.POST, "/api/packs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/packs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/packs/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/preferences/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/preferences/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/preferences/**").hasRole("STUDENT")

                        // --- Secure sensitive READ/other Endpoints ---
                        .requestMatchers("/actuator/metrics").hasRole("ADMIN")
                        .requestMatchers("/actuator/**").authenticated()

                        // --- NEW RULE: Permit all other GET requests ---
                        // This allows GET /api/** AND lets GET /users proceed to the 404 page
                        .requestMatchers(HttpMethod.GET).permitAll()

                        // --- NEW CATCH-ALL: Deny any other request ---
                        // (e.g., POST /users, POST /some-random-url)
                        .anyRequest().denyAll()
                );

        // 4. Add the JWT filter (this part is unchanged)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}