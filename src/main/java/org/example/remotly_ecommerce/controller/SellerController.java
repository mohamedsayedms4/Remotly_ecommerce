package org.example.remotly_ecommerce.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.service.SellerService;
import org.example.remotly_ecommerce.utilis.ImageUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;
    private final ImageUploadUtil imageUploadUtil;



//
//    @PostMapping("/signup")
//    public ResponseEntity<AuthResponse> createUser(
//            @Valid @RequestPart("seller_details") String sellerDetails,
//            @RequestPart(value = "image", required = false) MultipartFile image)throws Exception {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        SignUpRequest signUpRequest = objectMapper.readValue(sellerDetails, SignUpRequest.class);
//        // معالجة الصورة إذا كانت موجودة
//        String imageUrl = null;
//        if (image != null && !image.isEmpty()) {
//            imageUrl = imageUploadUtil.saveImage(image);
//        }
//
//
//        // إنشاء request جديد مع URL الصورة
//        SignUpRequest finalRequest = new SignUpRequest(
//                signUpRequest.customerEmail(),
//                signUpRequest.customerFullName(),
//                signUpRequest.customerPhoneNumber(),
//                imageUrl,
//                signUpRequest.customerPassword()
//        );
//
//        String jwt = sellerService.createSeller(finalRequest)
//                .orElseThrow(() -> new RuntimeException("Failed to create seller"));
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setJwt(jwt);
//        authResponse.setMessage("User created successfully");
//        authResponse.setUserRole(UserRole.ROLE_SELLER);
//
//        return ResponseEntity.ok(authResponse);
//    }
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String token = sellerService.login(request).orElseThrow(() -> new RuntimeException("Failed to login"));

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .build();
    }

    @PostMapping("/profile/jwt")
    public ResponseEntity<?> getUserProfileByJwt(
            @RequestHeader(value = "Authorization", required = false) String jwt) {


        if (jwt == null || jwt.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header is missing"));
        }

        try {
            Optional<Seller> seller = sellerService.getSellerProfile(jwt);

            if (seller.isPresent()) {
                System.out.println("User found: " + seller.get().getEmail());
                return ResponseEntity.ok(seller.get());
            } else {
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
    @GetMapping("/profile/byId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSellerById(@RequestParam Long id) throws SellerException {
        Optional<Seller> seller = sellerService.getSellerById(id);
        log.info("Received request to search seller with id: {}", id);
        if (seller.isPresent()) {
            log.debug("Seller found: {}", seller.get());
            return ResponseEntity.ok(seller.get());
        } else {
            log.error("Seller not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Seller not found"));
        }
    }
    @GetMapping("/profile/email")
    public ResponseEntity<?> getSellerByEmail(@RequestParam String email) throws SellerException {
        log.info("Received request to search seller with customerEmail: {}", email);

        Optional<Seller> seller = sellerService.getSellerByEmail(email);

        if (seller.isPresent()) {
            log.debug("Seller found: {}", seller.get());
            return ResponseEntity.ok(seller.get());
        } else {
            log.error("Seller not found for customerEmail: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Seller not found"));
        }
    }


    @PutMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSellerAccountStatus(@RequestParam Long id, @RequestBody AccountStatus status) throws SellerException {
        Optional<Seller> sellerOpt = sellerService.getSellerById(id);
        if (sellerOpt.isPresent()) {
            Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status).orElse(null);
            if (updatedSeller != null) {
                return ResponseEntity.ok(updatedSeller);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update seller status");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found");
    }


    @DeleteMapping("/del")
     public ResponseEntity<?> deleteSeller(@RequestParam Long id) throws SellerException {
        Optional<Seller> sellerOpt = sellerService.getSellerById(id);
        if (sellerOpt.isPresent()) {
            sellerService.deleteSellerById(id);
            return ResponseEntity.ok(Map.of("message", "Seller deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Seller not found"));
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSellers() {
        List<Seller> sellers = sellerService.getAllSellers();
        if (sellers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No sellers found");
        }
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/all/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSellers(@PathVariable AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        if (sellers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No sellers found");
        }
        return ResponseEntity.ok(sellers);
    }


    @PutMapping("/verified")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> verifySeller (
            @RequestParam Long id,
            @RequestParam Boolean emailVerified) throws SellerException {

        Optional<Seller> sellerOpt = sellerService.getSellerById(id);
        if (sellerOpt.isPresent()) {
            Optional<Seller> updatedSellerOpt = sellerService.verifySeller(id, emailVerified);
            if (updatedSellerOpt.isPresent()) {
                return ResponseEntity.ok(updatedSellerOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to update seller verification status");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Seller not found with id: " + id);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSeller(@RequestParam String search) {
        List<SellerDto> sellers = sellerService.getSellersByBusinessName(search);
        if (sellers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No sellers found");
        }
        return ResponseEntity.ok(sellers);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(
            @RequestPart("seller_details") String userDetailsJson,
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
        String jwt = sellerService.createSeller(finalRequest)
                .orElseThrow(() -> new RuntimeException("Failed to create customer"));

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setUserRole(UserRole.ROLE_SELLER);

        return ResponseEntity.ok(authResponse);
    }

}
