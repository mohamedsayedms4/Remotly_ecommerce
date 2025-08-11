package org.example.remotly_ecommerce.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.example.remotly_ecommerce.model.User;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final Environment environment;

    public JwtService(Environment environment) {
        this.environment = environment;
    }

    public String generateToken(Authentication authentication) {
        String secret = environment.getProperty(
                ApplicationConstants.JWT_SECRET_KEY,
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE
        );

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer("Eazy bank")
                .setSubject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3000000))
                .signWith(secretKey)
                .compact();
    }

    public String generateToken(User user) {
        String secret = environment.getProperty(
                ApplicationConstants.JWT_SECRET_KEY,
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE
        );

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer("Eazy bank")
                .setSubject("JWT Token")
                .claim("username", user.getEmail())
                .claim("authorities", user.getAuthorities().stream()
                        .map(authority -> authority.getRole().name())  // هنا
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3000000))
                .signWith(secretKey)
                .compact();
    }
}
