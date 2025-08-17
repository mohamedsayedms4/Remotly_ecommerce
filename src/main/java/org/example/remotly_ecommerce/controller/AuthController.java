package org.example.remotly_ecommerce.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.helper.user.sign_up.controller.SignUpContext;
import org.example.remotly_ecommerce.helper.user.sign_up.controller.SignUpControllerHelper;
import org.example.remotly_ecommerce.helper.user.sign_up.controller.SignUpStrategy;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.service.AuthService;
import org.example.remotly_ecommerce.utilis.ImageUploadUtil;
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
    private final SignUpControllerHelper signUpControllerHelper;
    private final SignUpContext signUpContext;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(
        @RequestPart("user_details") String userDetailsJson,
        @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        SignUpRequest finalRequest = signUpControllerHelper.buildSignUpRequest(userDetailsJson, image);

        SignUpStrategy strategy = signUpContext.getStrategy("userSignUpStrategy");
        if (strategy == null) {
            throw new RuntimeException("No strategy found for type");
        }
         AuthResponse response = strategy.signUp(finalRequest);

        return ResponseEntity.ok(response);
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
