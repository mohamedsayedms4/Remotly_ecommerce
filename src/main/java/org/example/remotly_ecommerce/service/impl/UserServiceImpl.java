package org.example.remotly_ecommerce.service.impl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.service.UserService;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${" + ApplicationConstants.JWT_SECRET_KEY + ":" + ApplicationConstants.JWT_SECRET_DEFAULT_VALUE + "}")
    private String jwtSecret;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user;
    }

//    @Override
//    public Optional<User> findByJwt(String jwt) {
//        try {
//             Bearer prefix
//            jwt = cleanJwtToken(jwt);
//
//            if (jwt == null || jwt.trim().isEmpty()) {
//                System.out.println("JWT is null or empty");
//                return Optional.empty();
//            }
//
//            // For JJWT 0.12.x and above
//            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//
//            Claims claims = Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(jwt)
//                    .getPayload();
//
//            String customerEmail = (String) claims.get("username");
//
//             subject
//            if (customerEmail == null || customerEmail.trim().isEmpty() || !customerEmail.contains("@")) {
//                customerEmail = claims.getSubject();
//            }
//
//            if (customerEmail == null || customerEmail.trim().isEmpty() || !customerEmail.contains("@") || customerEmail.equals("JWT Token")) {
//                System.out.println("No valid customerEmail found in JWT. Subject: " + claims.getSubject() + ", Username: " + claims.get("username"));
//                return Optional.empty();
//            }
//
//            System.out.println("Extracted customerEmail from JWT: " + customerEmail);
//
//            Optional<User> user = userRepository.findByEmail(customerEmail);
//            if (user.isEmpty()) {
//                System.out.println("User not found with customerEmail: " + customerEmail);
//            } else {
//                System.out.println("User found: " + user.get().getEmail());
//            }
//
//            return user;
//
//        } catch (Exception e) {
//            System.out.println("Failed to parse JWT: " + e.getMessage());
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }
//
//    /**
//     */
//    private String cleanJwtToken(String jwt) {
//        if (jwt == null) {
//            return null;
//        }
//
//        jwt = jwt.trim();
//
//        if (jwt.startsWith("Bearer ")) {
//            jwt = jwt.substring("Bearer ".length());
//        }
//
//        return jwt.trim();
//    }
@Override
public Optional<User> findByJwt(String jwt) {
    try {
        String email = jwtUtil.extractEmailFromJwt(jwt);
        return userRepository.findByEmail(email);
    } catch (Exception e) {
        System.out.println("Failed to extract user from JWT: " + e.getMessage());
        return Optional.empty();
    }
}

}