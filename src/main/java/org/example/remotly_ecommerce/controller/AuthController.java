package org.example.remotly_ecommerce.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.response.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;
import org.example.remotly_ecommerce.response.SignUpRequest;
import org.example.remotly_ecommerce.service.AuthService;
import org.example.remotly_ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        String jwt = authService.createUser(signUpRequest);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setUserRole(UserRole.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .build();
    }


    @PostMapping("/loginWithOtp")
    public ResponseEntity<String> loginWithOtp(@RequestBody LoginRequest request) {
        String response = authService.loginWithOtp(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody OtpVerificationRequest request) {
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }




}
