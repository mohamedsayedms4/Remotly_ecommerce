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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // نقرأ الـ secret key من الإعدادات أو نستخدم default value
    @Value("${" + ApplicationConstants.JWT_SECRET_KEY + ":" + ApplicationConstants.JWT_SECRET_DEFAULT_VALUE + "}")
    private String jwtSecret;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        // إزالة الـ exception ورجوع Optional.empty() بدلاً منها
        return user;
    }

    @Override
    public Optional<User> findByJwt(String jwt) {
        try {
            // تنظيف الـ JWT من أي مسافات أو Bearer prefix
            jwt = cleanJwtToken(jwt);

            // التأكد من وجود الـ JWT
            if (jwt == null || jwt.trim().isEmpty()) {
                System.out.println("JWT is null or empty");
                return Optional.empty();
            }

            // For JJWT 0.12.x and above
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            // محاولة الحصول على email من username claim أولاً (لأن الـ subject = "JWT Token")
            String email = (String) claims.get("username");

            // إذا username مش موجود، جرب subject
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                email = claims.getSubject();
            }

            // التأكد من وجود email صحيح
            if (email == null || email.trim().isEmpty() || !email.contains("@") || email.equals("JWT Token")) {
                System.out.println("No valid email found in JWT. Subject: " + claims.getSubject() + ", Username: " + claims.get("username"));
                return Optional.empty();
            }

            System.out.println("Extracted email from JWT: " + email);

            // البحث عن الـ User
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                System.out.println("User not found with email: " + email);
            } else {
                System.out.println("User found: " + user.get().getEmail());
            }

            return user;

        } catch (Exception e) {
            // يمكنك إضافة logging هنا لمعرفة سبب فشل parsing الـ JWT
            System.out.println("Failed to parse JWT: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * تنظيف الـ JWT token من Bearer prefix والمسافات
     */
    private String cleanJwtToken(String jwt) {
        if (jwt == null) {
            return null;
        }

        // إزالة المسافات من البداية والنهاية
        jwt = jwt.trim();

        // إزالة "Bearer " إذا كان موجود
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring("Bearer ".length());
        }

        return jwt.trim();
    }
}