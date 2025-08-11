package org.example.remotly_ecommerce.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);

        if (jwt != null) {
            try {
                // إزالة "Bearer " من بداية الـ token إذا كان موجود
                if (jwt.startsWith("Bearer ")) {
                    jwt = jwt.substring(7);
                }

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

                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                // استخدام subject بدلاً من username claim للتوافق مع UserService
                String email = claims.getSubject();
                if (email == null) {
                    // fallback للـ username claim إذا ما في subject
                    email = String.valueOf(claims.get("username"));
                }

                String authoritiesClaim = claims.get("authorities", String.class);

                List<GrantedAuthority> grantedAuthorities;
                if (authoritiesClaim != null && !authoritiesClaim.isEmpty()) {
                    grantedAuthorities = Arrays.stream(authoritiesClaim.split(","))
                            .filter(auth -> auth != null && !auth.trim().isEmpty())
                            .map(auth -> {
                                if (!auth.startsWith("ROLE_")) {
                                    return new SimpleGrantedAuthority("ROLE_" + auth);
                                } else {
                                    return new SimpleGrantedAuthority(auth);
                                }
                            })
                            .collect(Collectors.toList());
                } else {
                    // إذا ما في صلاحيات، استخدم ROLE_USER الافتراضية
                    grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.out.println("JWT Validation Error: " + e.getMessage());
                // لا ترمي exception، فقط اتجاهل الـ token الخاطئ
                // throw new BadCredentialsException("Invalid JWT token", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/auth/signup")
                || request.getServletPath().equals("/auth/login")
                || request.getServletPath().equals("/auth/loginWithOtp")
                || request.getServletPath().equals("/auth/verifyOtp");
    }
}