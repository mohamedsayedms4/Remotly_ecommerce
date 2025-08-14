package org.example.remotly_ecommerce.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.exception.InvalidEmail;
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


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository ;
    private final EmailService emailService;
    private final JwtService jwtService;


    @Override
    public String createUser(SignUpRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        User savedUser = new User();
        savedUser.setEmail(request.getEmail());
        savedUser.setPhoneNumber(request.getPhoneNumber());
        savedUser.setFullName(request.getFullName());
        savedUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Authority authority = new Authority();
        authority.setRole(UserRole.ROLE_ADMIN);
        authority.setCustomer(savedUser);
        savedUser.getAuthorities().add(authority);

        userRepository.save(savedUser);

        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);

        String jwtToken = jwtService.generateToken(savedUser);


        return jwtToken;
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

        String jwtToken = jwtService.generateToken(user);
        UserRole role = user.getAuthorities().iterator().next().getRole();
        return jwtToken;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public String loginWithOtp(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidEmail("Invalid email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPWD("Invalid password");
        }

        String otp = String.format("%06d", (int)(Math.random() * 1000000));

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

        emailService.sendVerificationCode(user.getEmail(), otp, "Your OTP Code", "Use this code to login.");

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

                String jwtToken = jwtService.generateToken(user);

                UserRole userRole = null;
                if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
                    userRole = user.getAuthorities().iterator().next().getRole();
                } else {
                    throw new RuntimeException("User has no authorities assigned");
                }

                AuthResponse response = new AuthResponse();
                response.setJwt(jwtToken);
                response.setMessage("OTP verified successfully");
                response.setUserRole(userRole);

                return response;

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }



}
