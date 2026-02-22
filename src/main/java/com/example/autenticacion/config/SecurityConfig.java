package com.example.autenticacion.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.example.autenticacion.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

   
    
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    return http
      .csrf(csrf -> 
      csrf
      .disable())
    .authorizeHttpRequests(authRequest ->
      authRequest
      //Rutas públicas (sin autenticación)
      .requestMatchers("/auth/login",
                      "/auth/login-android",
                      "/auth/register",
                      "/auth/refresh",
                      "/auth/forgot-password",
                      "/auth/reset-password",
                      "/api-docs",          
                      "/api-docs/**",        
                      "/swagger-ui/**", 
                      "/swagger-ui.html").permitAll()

      //Rutas protegidas de auth (requieren autenticación)
      .requestMatchers("/auth/me",
                        "/auth/logout",
                        "/auth/logout-all",
                        "/auth/change-password",
                        "/auth/profile").authenticated()
                      .requestMatchers("/admin/**").hasRole("ADMIN")
                      .requestMatchers("/seller/**").hasRole("SELLER")
                      .anyRequest().authenticated()
    )
    .sessionManagement(sessionManager -> 
      sessionManager
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authProvider)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
    .build();
  }



  
}

