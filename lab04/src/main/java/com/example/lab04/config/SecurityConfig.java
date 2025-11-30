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
import jakarta.servlet.DispatcherType;
//annotation for preauthorize
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //bean for bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                //cross-site request forgery
                .csrf(AbstractHttpConfigurer::disable)

                //stateless api
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //authorization rules
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll()
                        //public endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/grades/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasAnyRole("ADMIN", "INSTRUCTOR")

                        .requestMatchers(HttpMethod.POST, "/api/instructor-preferences/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/instructor-preferences/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/instructor-preferences/**").hasAnyRole("ADMIN", "INSTRUCTOR")

                        .requestMatchers(HttpMethod.POST, "/api/matching/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/matching/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/matching/**").hasAnyRole("ADMIN", "INSTRUCTOR")
//                        .requestMatchers("/api/matching/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/packs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/packs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/packs/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/preferences/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/preferences/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/preferences/**").hasRole("STUDENT")

                        .requestMatchers("/actuator/metrics").hasRole("ADMIN")
                        .requestMatchers("/actuator/**").authenticated()

                        .requestMatchers(HttpMethod.GET).permitAll()

                        .anyRequest().denyAll()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}