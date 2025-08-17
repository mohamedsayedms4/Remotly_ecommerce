package org.example.remotly_ecommerce.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.example.remotly_ecommerce.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;


@Service
public class JwtService {

    @Value("${jwt.expiration:300000000000000}")
    private long jwtExpiration;

    public String generateToken(User user) {
        String secret = System.getenv(ApplicationConstants.JWT_SECRET_KEY);
        if (secret == null || secret.isBlank()) {
            secret = ApplicationConstants.JWT_SECRET_DEFAULT_VALUE;
        }

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer("Masala")
                .setSubject(user.getEmail())
                .claim("username", user.getEmail())
                .claim("authorities", user.getAuthorities().stream()
                        .map(a -> a.getRole().name())
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }
}
