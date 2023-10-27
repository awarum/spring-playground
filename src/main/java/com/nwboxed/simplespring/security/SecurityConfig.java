package com.nwboxed.simplespring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BearerTokenRequestFilter bearerTokenRequestFilter;

    private static final String[] UNSECURED_ENDPOINTS = {"/v3/api-docs/**","/swagger-ui/**"};


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .requestMatchers(UNSECURED_ENDPOINTS).permitAll()
                .requestMatchers("/api/cars/**").hasAuthority("USER")
                .anyRequest().denyAll()
                .and()
                //.httpBasic(Customizer.withDefaults())
                .addFilterBefore(bearerTokenRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
