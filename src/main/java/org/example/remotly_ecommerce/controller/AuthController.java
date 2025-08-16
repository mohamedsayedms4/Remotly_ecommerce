package org.example.remotly_ecommerce.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.service.AuthService;
import org.example.remotly_ecommerce.utilis.ImageUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final ImageUploadUtil imageUploadUtil;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(
            @RequestPart("user_details") String userDetailsJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        // تحويل JSON string إلى SignUpRequest object
        ObjectMapper objectMapper = new ObjectMapper();
        SignUpRequest signUpRequest = objectMapper.readValue(userDetailsJson, SignUpRequest.class);

        // معالجة الصورة إذا كانت موجودة
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageUploadUtil.saveImage(image);
        }

        // إنشاء request جديد مع URL الصورة
        SignUpRequest finalRequest = new SignUpRequest(
                signUpRequest.customerEmail(),
                signUpRequest.customerFullName(),
                signUpRequest.customerPhoneNumber(),
                imageUrl,
                signUpRequest.customerPassword()
        );

        // استدعاء الـ service باستخدام الـ request الجديد
        String jwt = authService.createCustomer(finalRequest)
                .orElseThrow(() -> new RuntimeException("Failed to create customer"));

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
