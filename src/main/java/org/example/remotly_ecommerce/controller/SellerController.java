package org.example.remotly_ecommerce.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.dto.BecomeASellerDto;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.helper.user.sign_up.controller.SignUpContext;
import org.example.remotly_ecommerce.helper.user.sign_up.controller.SignUpControllerHelper;
import org.example.remotly_ecommerce.helper.user.sign_up.controller.SignUpStrategy;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.service.SellerService;
import org.example.remotly_ecommerce.utilis.ResponseHelperUtil;
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
    private final SignUpControllerHelper signUpControllerHelper;
    private final SignUpContext signUpContext;



    /**
     * Handles the signup request for a seller.
     * This endpoint accepts seller details as a JSON string and an optional profile image.
     * It uses the Strategy Pattern to delegate the actual signup logic to the appropriate strategy.
     *
     * @param userDetailsJson JSON string containing seller details.
     * @param image Optional MultipartFile representing the seller's profile image.
     * @return ResponseEntity containing AuthResponse with JWT, message, and user role.
     * @throws Exception if JSON parsing fails or image processing fails.
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(
            @RequestPart("seller_details") String userDetailsJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        // Convert JSON string + handle optional image into SignUpRequest using the helper
        SignUpRequest finalRequest = signUpControllerHelper.buildSignUpRequest(userDetailsJson, image);
        log.info("finalRequest : {}", finalRequest.toString());

        // Select the appropriate signup strategy using Strategy Pattern
        SignUpStrategy strategy = signUpContext.getStrategy("sellerSignUpStrategy");
        if (strategy == null) {
            // Throw exception if no strategy is found
            throw new RuntimeException("No strategy found for type");
        }

        // Execute the selected strategy to perform signup and get AuthResponse
        AuthResponse response = strategy.signUp(finalRequest);

        // Return the final response with JWT, message, and role
        return ResponseEntity.ok(response);
    }





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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "JWT processing failed", "details", e.getMessage()));
        }
    }
    @GetMapping("/profile/byId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSellerById(@RequestParam Long id) throws SellerException {
        Optional<Seller> seller = sellerService.getSellerById(id);
        return ResponseHelperUtil.fromOptional(seller, "Seller not found");
    }



    @GetMapping("/profile/email")
    public ResponseEntity<?> getSellerByEmail(@RequestParam String email) throws SellerException {
        Optional<Seller> seller = sellerService.getSellerByEmail(email);
        return ResponseHelperUtil.fromOptional(seller, "Seller not found");
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
        return ResponseHelperUtil.fromOptional(sellerOpt, "Seller not found");
    }


    @DeleteMapping("/del")
    @PreAuthorize("hasRole('ADMIN')")
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


    @PostMapping("/become")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<?> becomeASeller(
            @RequestParam Long userId,
            @RequestBody BecomeASellerDto dto) {
        log.info("SellerController: becomeASeller called with userId {} and dto {}", userId, dto);

        try {
            Optional<Seller> sellerOpt = sellerService.becomeASeller(userId, dto);
            if (sellerOpt.isPresent()) {
                log.info("Seller promotion successful for userId {}", userId);
                return ResponseEntity.ok(Map.of(
                        "message", "User promoted to Seller successfully",
                        "sellerId", sellerOpt.get().getId(),
                        "accountStatus", sellerOpt.get().getAccountStatus().name()
                ));
            } else {
                log.warn("Seller promotion failed, seller not created for userId {}", userId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Could not create seller"));
            }
        } catch (SellerException ex) {
            log.error("Error in becomeASeller for userId {}: {}", userId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Unexpected error in becomeASeller for userId {}: {}", userId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

}
