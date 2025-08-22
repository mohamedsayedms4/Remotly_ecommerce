package org.example.remotly_ecommerce.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.user.ChangeUserPWD;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.dto.user.UserUpdateDto;
import org.example.remotly_ecommerce.dto.user.UserUpdateProfileImageDto;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.example.remotly_ecommerce.exception.InvalidPhoneNumber;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.service.user.UserService;
import org.example.remotly_ecommerce.utilis.ImageUploadUtil;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ImageUploadUtil imageUploadUtil;
    @GetMapping("profile/email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserProfileByEmail( @RequestParam
                                                        @NotBlank(message = "{email.invalid.blank.input.from.user}")   // لو الايميل فاضي
                                                        @Email(message = "{email.invalid.format.input.from.user}")     // لو الايميل مش صحيح
                                                        String email)
                                                         {
        Optional<UserFullInformationDto> user = userService.findByEmail(email, UserFullInformationDto.class);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/profile/id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserProfileById(
            @NotBlank(message = "id cannot be blank")
            @Pattern(regexp = "^[0-9]+$",message = "Only numbers")
            @RequestParam("id") String id) {
        Optional<UserFullInformationDto> user = userService.findById(id,UserFullInformationDto.class);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("profile/jwt")
    public ResponseEntity<?> getUserProfileByJwt(
            @RequestHeader(value = "Authorization", required = false) String jwt) {

        log.info("----------JWT IS______________ : {}",jwt);
        if (jwt == null || jwt.trim().isEmpty()) {
            log.error("JWT is null or empty");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header is missing"));
        }

        try {
            Optional<UserFullInformationDto> user = userService.findByJwt(jwt,UserFullInformationDto.class);

            if (user.isPresent()) {
                log.info("UserFullInformationDto : {}", user.get().email());
                System.out.println("User found: " + user.get().email());
                return ResponseEntity.ok(user.get());
            } else {
                log.error("User not found");
                System.out.println("User not found or invalid JWT");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "error", "User not found",
                                "message", "Either the JWT is invalid or the user doesn't exist in database"
                        ));
            }
        } catch (Exception e) {
            System.out.println("Exception in getUserProfile: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "JWT processing failed", "details", e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<UserFullInformationDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return userService.getAllUsers(page, size);
    }

    // update user info
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto userFullInfo,
                                        @RequestHeader(value = "Authorization", required = false) String jwt)
    throws InvalidEmail, InvalidPhoneNumber {
        log.info("Received request to update user: {}", userFullInfo);

        if (jwt == null || jwt.trim().isEmpty()) {
            log.warn("Authorization header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header is missing"));
        }

        String email = jwtUtil.extractEmailFromJwt(jwt);
        log.debug("Extracted email from JWT: {}", email);

        Optional<UserUpdateDto> updatedUser = userService.updateUser(userFullInfo, email);

        if (updatedUser.isPresent()) {
            log.info("User with email [{}] updated successfully", email);
            return ResponseEntity.ok(updatedUser.get());
        } else {
            log.error("Failed to update user with email [{}]", email);
            return ResponseEntity.badRequest().body("User update failed");
        }
    }

    @PutMapping("/image")
    public ResponseEntity<?> updateImageUrl(
            @RequestPart(value = "imageProfile", required = true) MultipartFile imageProfile,
            @RequestHeader(value = "Authorization", required = false) String jwt
    ){
        log.info("Received request to update user: {}", imageProfile);

        if (jwt == null || jwt.trim().isEmpty()) {
            log.warn("Authorization header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header is missing"));
        }

        String email = jwtUtil.extractEmailFromJwt(jwt);
        log.debug("Extracted email from JWT: {}", email);

        // رفع الصور
        String imageUrl = null;
        if (imageProfile != null && !imageProfile.isEmpty()) {
            imageUrl = imageUploadUtil.saveImage(imageProfile);
        }

        Optional<UserUpdateProfileImageDto> updatedUser = userService.updateProfileImage(imageUrl, email);

        if (updatedUser.isPresent()) {
            log.info("User with email [{}] updated successfully", email);
            return ResponseEntity.ok(updatedUser.get());
        } else {
            log.error("Failed to update user with email [{}]", email);
            return ResponseEntity.badRequest().body("User update failed");
        }

    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(
            @RequestHeader(value = "Authorization", required = false) String jwt
    ) {
        log.info("Received request to delete user with jwt: {}", jwt);

        if (jwt == null || jwt.trim().isEmpty()) {
            log.warn("Authorization header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header is missing"));
        }

        String email = jwtUtil.extractEmailFromJwt(jwt);
        log.debug("Extracted email from JWT: {}", email);

        Long id = userService.findUserByEmail(email);

        if (id == null) {
            log.error("Invalid email: {}", email);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid email address"));
        }

        userService.deleteUser(id);
        log.info("User with id [{}] deleted successfully", id);

        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PutMapping("/changePwd")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangeUserPWD changeUserPWD ,
            @RequestHeader(value = "Authorization", required = false) String jwt
            ){
        log.info("Received request to delete user with jwt: {}", jwt);

        if (jwt == null || jwt.trim().isEmpty()) {
            log.warn("Authorization header is missing");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header is missing"));
        }

        String email = jwtUtil.extractEmailFromJwt(jwt);
        log.debug("Extracted email from JWT: {}", email);

        if(!email.equals(changeUserPWD.email())){
            throw new InvalidEmail("Invalid email address");
        }
        userService.updatePassword(changeUserPWD);
        log.info("User with email [{}] updated successfully", changeUserPWD.email());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
//    @GetMapping("/profile/id")
//    public ResponseEntity<?> getUserProfileById(@RequestParam String id) {
//
//    }


}
