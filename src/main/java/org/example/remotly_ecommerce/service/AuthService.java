package org.example.remotly_ecommerce.service;


import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.response.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;
import org.example.remotly_ecommerce.response.SignUpRequest;

public interface AuthService {
    String createUser(SignUpRequest request);
    String login(LoginRequest request);

    String loginWithOtp(LoginRequest request); // توليد وإرسال OTP

    String verifyOtp(String email, String otp);
    // تحقق من OTP وتوليد JWT

    AuthResponse verifyOtp(OtpVerificationRequest request);

}
