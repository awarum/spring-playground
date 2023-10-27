package com.nwboxed.simplespring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class BearerTokenRequestFilter extends OncePerRequestFilter {

    @Autowired
    InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeaderValue = request.getHeader("Authorization");
        String username = null;
        String password = null;
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer ")) {
            System.out.println(authorizationHeaderValue);
            String[] credentials = readCredentials(authorizationHeaderValue);
            username = credentials[0];
            password = credentials[1];
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(username);



            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);

            }


        }

        filterChain.doFilter(request, response);
    }

    private String[] readCredentials(String authorizationHeaderValue) {
        if (authorizationHeaderValue.startsWith("Bearer ")) {
            String encodedCredentials = authorizationHeaderValue.substring(7);
            String decodedCredentials = new String(java.util.Base64.getDecoder().decode(encodedCredentials));
            String[] credentials = decodedCredentials.split(":", 2);
            return credentials;
        }
        else {
            throw new RuntimeException("No Bearer token");
        }
    }
}
