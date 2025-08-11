package org.example.remotly_ecommerce.Filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Environment environment = getEnvironment();
            String secret;
            if (environment != null) {
                secret = environment.getProperty(
                        ApplicationConstants.JWT_SECRET_KEY,
                        ApplicationConstants.JWT_SECRET_DEFAULT_VALUE
                );
            } else {
                secret = ApplicationConstants.JWT_SECRET_DEFAULT_VALUE;
            }

            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            String jwt = Jwts.builder()
                    .issuer("Remotly Ecommerce")
                    .subject(authentication.getName()) // استخدام subject بدلاً من claim للتوافق مع UserService
                    .claim("username", authentication.getName()) // الاحتفاظ بـ username claim للتوافق الخلفي
                    .claim("authorities", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + 3000000)) // 50 دقيقة
                    .signWith(secretKey)
                    .compact();

            response.setHeader(ApplicationConstants.JWT_HEADER, jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/auth/login");
    }
}