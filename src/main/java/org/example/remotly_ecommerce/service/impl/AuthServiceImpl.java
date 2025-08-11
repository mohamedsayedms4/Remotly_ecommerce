package org.example.remotly_ecommerce.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.constants.ApplicationConstants;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.example.remotly_ecommerce.exception.InvalidOtp;
import org.example.remotly_ecommerce.exception.InvalidPWD;
import org.example.remotly_ecommerce.exception.UserAlreadyExistsException;
import org.example.remotly_ecommerce.model.Authority;
import org.example.remotly_ecommerce.model.Cart;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.model.VerificationCode;
import org.example.remotly_ecommerce.repository.CartRepository;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.repository.VerificationCodeRepository;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.response.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;
import org.example.remotly_ecommerce.response.SignUpRequest;
import org.example.remotly_ecommerce.service.AuthService;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final Environment environment;
    private final VerificationCodeRepository verificationCodeRepository ;
    private final EmailService emailService;
    private final JwtService jwtService;


    @Override
    public String createUser(SignUpRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        // إنشاء المستخدم
        User savedUser = new User();
        savedUser.setEmail(request.getEmail());
        savedUser.setPhoneNumber(request.getPhoneNumber());
        savedUser.setFullName(request.getFullName());
        savedUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Authority authority = new Authority();
        authority.setRole(UserRole.ROLE_SELLER);
        authority.setCustomer(savedUser);
        savedUser.getAuthorities().add(authority);

        userRepository.save(savedUser);

        // إنشاء عربة تسوق
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        // 🔑 توليد الـ JWT مباشرة بعد إنشاء المستخدم
        String secret = environment.getProperty(
                ApplicationConstants.JWT_SECRET_KEY,
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE
        );

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
                .setIssuer("Eazy bank")
                .setSubject("JWT Token")
                .claim("username", savedUser.getEmail())
                .claim("authorities", savedUser.getAuthorities().stream()
                        .map(a -> a.getRole().name())
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3000000)) // ~50 دقيقة
                .signWith(secretKey)
                .compact();

        return jwt; // رجّع التوكن بدل النص العادي
    }

    /**
     * @param request
     * @return
     */
    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidEmail("Invalid email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPWD("Invalid password");
        }

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
                        .map(a -> a.getRole().name())
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3000000))
                .signWith(secretKey)
                .compact();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public String loginWithOtp(LoginRequest request) {
        // 1. تحقق وجود المستخدم وكلمة المرور
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidEmail("Invalid email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPWD("Invalid password");
        }

        // 2. توليد كود OTP عشوائي
        String otp = String.format("%06d", (int)(Math.random() * 1000000));

        // 3. حفظ الكود في جدول VerificationCode (تحديث أو إضافة جديد)
        VerificationCode existingCode = verificationCodeRepository.findByEmail(user.getEmail());
        if (existingCode != null) {
            existingCode.setOtp(otp);
            verificationCodeRepository.save(existingCode);
        } else {
            VerificationCode newCode = new VerificationCode();
            newCode.setEmail(user.getEmail());
            newCode.setOtp(otp);
            newCode.setUser(user);
            verificationCodeRepository.save(newCode);
        }

        // 4. إرسال الإيميل مع الـ OTP (تحتاج تستدعي خدمة الإيميل هنا)
        emailService.sendVerificationCode(user.getEmail(), otp, "Your OTP Code", "Use this code to login.");

        // 5. هنا ما ترجع JWT — ترجع رسالة أو رمز نجاح أن OTP أُرسل
        return "OTP sent to email";
    }

    /**
     * @param email
     * @param otp
     * @return
     */
    @Override
    public String verifyOtp(String email, String otp) {
        return "";
    }

    /**
     * @param request
     * @return
     */
    @Override
    public AuthResponse verifyOtp(OtpVerificationRequest request) {

            try {
                String email = request.getEmail();
                String otp = request.getOtp();

                VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
                if (verificationCode == null) {
                    throw new RuntimeException("No OTP request found for this email");
                }

                if (!verificationCode.getOtp().equals(otp)) {
                    throw new RuntimeException("Invalid OTP code");
                }

                User user = verificationCode.getUser();

                verificationCodeRepository.delete(verificationCode);

                // توليد JWT باستخدام JwtService
                String jwtToken = jwtService.generateToken(user);

                // استخراج أول دور (Role) من authorities أو رمي استثناء إذا لم توجد صلاحيات
                UserRole userRole = null;
                if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
                    userRole = user.getAuthorities().iterator().next().getRole();
                } else {
                    throw new RuntimeException("User has no authorities assigned");
                }

                // إعداد الرد
                AuthResponse response = new AuthResponse();
                response.setJwt(jwtToken);
                response.setMessage("OTP verified successfully");
                response.setUserRole(userRole);

                return response;

                // كود التحقق اللي عندك
            } catch (Exception e) {
                e.printStackTrace();
                throw e; // أو ترجع رسالة مناسبة
            }
        }



}
