package org.example.remotly_ecommerce.controller;


import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.response.SignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getOtp());
        user.setRole(UserRole.ROLE_CUSTOMER);
        user.setFullName(request.getFullName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/8")
    public String login() {
        return "Hello World";
    }
}
