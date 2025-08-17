package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.dto.BecomeASellerDto;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;

import java.util.List;
import java.util.Optional;

/**
 * The {@code SellerService} interface defines the contract for managing seller accounts.
 * <p>
 * It provides operations for registration, authentication, profile management,
 * account verification, deletion, and retrieval of seller information.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Register new sellers and generate authentication tokens.</li>
 *     <li>Authenticate sellers using login credentials.</li>
 *     <li>Retrieve seller profiles by JWT, ID, or email.</li>
 *     <li>Update seller details, account status, and verification state.</li>
 *     <li>Delete sellers by entity or ID.</li>
 *     <li>Fetch sellers filtered by account status or business name.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
public interface SellerService {

    /**
     * Retrieves the seller profile using a JWT token.
     *
     * @param jwt the JWT token containing seller identity
     * @return an {@link Optional} containing the {@link Seller} profile if found
     */
    Optional<Seller> getSellerProfile(String jwt) throws SellerException;

    /**
     * Creates a new seller account.
     *
     * @param request the {@link SignUpRequest} containing seller details
     * @return an {@link Optional} containing a JWT token if registration succeeds
     */
    Optional<String> createSeller(SignUpRequest request);

    /**
     * Performs seller login using provided credentials.
     *
     * @param request the {@link LoginRequest} containing login details
     * @return an {@link Optional} containing a JWT token if authentication is successful
     */
    Optional<String> login(LoginRequest request);

    /**
     * Retrieves a seller by their ID.
     *
     * @param id the seller ID
     * @return an {@link Optional} containing the {@link Seller} if found
     * @throws SellerException if no seller is found with the given ID
     */
    Optional<Seller> getSellerById(Long id) throws SellerException;

    /**
     * Retrieves a seller by their email.
     *
     * @param email the seller email
     * @return an {@link Optional} containing the {@link Seller} if found
     * @throws SellerException if no seller is found with the given email
     */
    Optional<Seller> getSellerByEmail(String email) throws SellerException;

    /**
     * Updates a seller's details if they exist.
     *
     * @param seller the {@link Seller} entity to update
     * @return an {@link Optional} containing the updated {@link Seller}, or empty if not found
     */
    Optional<Seller> updateSeller(Seller seller);

    /**
     * Verifies a seller's email.
     *
     * @param id            the seller ID
     * @param emailVerified true if the email is verified, false otherwise
     * @return an {@link Optional} containing the updated {@link Seller}
     */
    Optional<Seller> verifySeller(Long id, Boolean emailVerified) throws SellerException;

    /**
     * Deletes a seller entity.
     *
     * @param seller the {@link Seller} to delete
     */
    void deleteSeller(Seller seller);

    /**
     * Updates the account status of a seller.
     *
     * @param id     the seller ID
     * @param status the new {@link AccountStatus}
     * @return an {@link Optional} containing the updated {@link Seller}, or empty if not found
     */
    Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status) throws SellerException;

    /**
     * Checks if a given {@link User} is a seller.
     *
     * @param user the user entity
     * @return true if the user is a seller, false otherwise
     */
    boolean isSeller(User user);

    /**
     * Retrieves all sellers.
     *
     * @return a list of all {@link Seller} entities
     */
    List<Seller> getAllSellers();

    /**
     * Retrieves all sellers filtered by account status.
     *
     * @param status the {@link AccountStatus} to filter sellers
     * @return a list of {@link Seller} entities with the given status
     */
    List<Seller> getAllSellers(AccountStatus status);

    /**
     * Deletes a seller by their ID.
     *
     * @param id the seller ID
     */
    void deleteSellerById(Long id);

    /**
     * Retrieves sellers filtered by business name.
     *
     * @param businessName the business name to filter
     * @return a list of {@link SellerDto} for matching sellers
     */
    List<SellerDto> getSellersByBusinessName(String businessName);


    Optional<Seller> becomeASeller(Long id ,BecomeASellerDto  becomeASellerDto ) throws SellerException;
}
