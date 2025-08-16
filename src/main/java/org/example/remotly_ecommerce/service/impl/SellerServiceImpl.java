package org.example.remotly_ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.helper.user.sign_in.LoginContext;
import org.example.remotly_ecommerce.helper.user.sign_up.Register;
import org.example.remotly_ecommerce.mapper.SellerMapper;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.CartRepository;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.service.SellerService;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link SellerService} interface for managing seller accounts.
 * <p>
 * This service handles seller registration, authentication, profile management,
 * account verification, and deletion. It integrates with JWT utilities, login strategies,
 * repositories, and mapping layers.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Registers new sellers and generates authentication tokens.</li>
 *     <li>Authenticates sellers using email/password strategy.</li>
 *     <li>Fetches seller profiles by ID or email.</li>
 *     <li>Updates seller details, account status, and verification state.</li>
 *     <li>Deletes sellers by entity or ID.</li>
 *     <li>Retrieves active sellers and filters by business name or status.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil;
    private final SellerMapper sellerMapper;
    private final LoginContext loginContext;
    private final Register register;

    /**
     * Retrieves a seller profile using a JWT token.
     *
     * @param jwt the JSON Web Token containing seller identity
     * @return an {@link Optional} containing the {@link Seller} profile if found
     */
    @Override
    public Optional<Seller> getSellerProfile(String jwt) {
        try {
            String email = jwtUtil.extractEmailFromJwt(jwt);
            return sellerRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("Failed to extract seller from JWT: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Creates a new seller account.
     *
     * @param request the {@link SignUpRequest} containing seller details
     * @return an {@link Optional} containing a JWT token if registration succeeds
     */
    @Override
    public Optional<String> createSeller(SignUpRequest request) {
        SignUpRequest sellerRequest = new SignUpRequest(
                request.customerEmail(),
                request.customerFullName(),
                request.customerPhoneNumber(),
                request.customerProfileImage(),
                request.customerPassword()
        );
        return register.registerAccount(sellerRequest, UserRole.ROLE_SELLER.toString());
    }

    /**
     * Performs seller login using email/password strategy.
     *
     * @param request the {@link LoginRequest} containing login details
     * @return an {@link Optional} containing a JWT token if authentication is successful
     */
    @Override
    public Optional<String> login(LoginRequest request) {
        AuthResponse authResponse = loginContext.executeLogin("emailPasswordLoginStrategy", request);
        return Optional.ofNullable(authResponse.getJwt());
    }

    /**
     * Retrieves a seller by ID.
     *
     * @param id the seller ID
     * @return an {@link Optional} containing the {@link Seller} if found
     * @throws SellerException if no seller is found with the given ID
     */
    @Override
    public Optional<Seller> getSellerById(Long id) throws SellerException {
        log.info("Searching for seller with id: {}", id);
        Optional<Seller> seller = sellerRepository.findById(id);
        if (seller.isEmpty()) {
            log.error("Seller not found for id: {}", id);
            throw new SellerException("Seller not found with ID: " + id);
        }
        log.debug("Seller found: {}", seller);
        return seller;
    }

    /**
     * Retrieves a seller by email.
     *
     * @param email the seller email
     * @return an {@link Optional} containing the {@link Seller} if found
     * @throws SellerException if no seller is found with the given email
     */
    @Override
    public Optional<Seller> getSellerByEmail(String email) throws SellerException {
        log.info("Searching for seller with customerEmail: {}", email);
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isEmpty()) {
            log.error("Seller not found for customerEmail: {}", email);
            throw new SellerException("Seller not found with customerEmail: " + email);
        }
        log.debug("Seller found: {}", seller);
        return seller;
    }

    /**
     * Updates a seller if it exists in the database.
     *
     * @param seller the {@link Seller} entity to update
     * @return an {@link Optional} containing the updated {@link Seller}, or empty if not found
     */
    @Override
    @Transactional
    public Optional<Seller> updateSeller(Seller seller) {
        if (seller.getId() != null && sellerRepository.existsById(seller.getId())) {
            Seller updatedSeller = sellerRepository.save(seller);
            return Optional.of(updatedSeller);
        }
        return Optional.empty();
    }

    /**
     * Verifies the email of a seller by updating their verification state.
     *
     * @param id            the seller ID
     * @param emailVerified whether the email is verified
     * @return an {@link Optional} containing the updated {@link Seller}
     */
    @Override
    @Transactional
    public Optional<Seller> verifySeller(Long id, Boolean emailVerified) {
        Optional<Seller> sellerOpt = sellerRepository.findById(id);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            seller.setIsEmailVerified(emailVerified);
            Seller updatedSeller = sellerRepository.save(seller);
            return Optional.of(updatedSeller);
        } else {
            throw new RuntimeException("Seller not found with id: " + id);
        }
    }

    /**
     * Deletes a seller entity.
     *
     * @param seller the {@link Seller} entity to delete
     */
    @Override
    @Transactional
    public void deleteSeller(Seller seller) {
        if (seller.getId() != null) {
            sellerRepository.deleteById(seller.getId());
        }
    }

    /**
     * Deletes a seller by ID.
     *
     * @param id the seller ID
     * @throws RuntimeException if no seller is found with the given ID
     */
    @Override
    @Transactional
    public void deleteSellerById(Long id) {
        if (sellerRepository.existsById(id)) {
            sellerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Seller not found with id: " + id);
        }
    }

    /**
     * Retrieves sellers filtered by business name and ensures they are active.
     *
     * @param businessName the business name filter
     * @return a list of {@link SellerDto} for active sellers matching the business name
     */
    @Override
    public List<SellerDto> getSellersByBusinessName(String businessName) {
        if (businessName == null || businessName.isEmpty()) {
            return Collections.emptyList();
        }
        List<Seller> sellers = sellerRepository.findByBusinessDetails_BusinessNameContainingIgnoreCase(businessName);
        return sellers.stream()
                .filter(s -> s.getAccountStatus() == AccountStatus.ACTIVE)
                .map(sellerMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the account status of a seller.
     *
     * @param id     the seller ID
     * @param status the new {@link AccountStatus}
     * @return an {@link Optional} containing the updated {@link Seller}, or empty if not found
     */
    @Override
    @Transactional
    public Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status) {
        Optional<Seller> sellerOpt = sellerRepository.findById(id);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            seller.setAccountStatus(status);
            sellerRepository.save(seller);
            return Optional.of(seller);
        }
        return Optional.empty();
    }

    /**
     * Checks if a given {@link User} is a seller.
     *
     * @param user the user entity
     * @return {@code true} if the user is a seller, otherwise {@code false}
     */
    @Override
    public boolean isSeller(User user) {
        return false;
    }

    /**
     * Retrieves all sellers.
     *
     * @return a list of all {@link Seller} entities
     */
    @Override
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    /**
     * Retrieves all sellers filtered by account status.
     *
     * @param status the {@link AccountStatus} to filter sellers
     * @return a list of {@link Seller} entities with the given status
     */
    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }
}
