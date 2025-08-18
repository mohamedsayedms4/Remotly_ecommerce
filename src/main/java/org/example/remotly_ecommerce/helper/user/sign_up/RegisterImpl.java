package org.example.remotly_ecommerce.helper.user.sign_up;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.exception.UserAlreadyExistsException;
import org.example.remotly_ecommerce.model.*;
import org.example.remotly_ecommerce.repository.CartRepository;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.service.impl.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Implementation of the {@link Register} interface that handles
 * user and seller account registration.
 * <p>
 * This service is responsible for creating new accounts, encoding
 * passwords, saving users/sellers to the database, assigning roles,
 * creating associated carts, and generating authentication tokens.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *     <li>Registers customer accounts with default ROLE_CUSTOMER</li>
 *     <li>Registers seller accounts with default ROLE_SELLER</li>
 *     <li>Initializes a shopping cart for every new account</li>
 *     <li>Generates JWT tokens upon successful registration</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RegisterImpl implements Register {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registers a new account (User or Seller) based on the provided request and role.
     *
     * @param request the {@link SignUpRequest} containing customer details
     * @param role    the role of the account to register (e.g., "ROLE_CUSTOMER", "ROLE_SELLER")
     * @return an {@link Optional} containing the generated JWT token if registration succeeds
     * @throws UserAlreadyExistsException if a user with the same email already exists
     * @throws IllegalArgumentException   if the provided role is unsupported
     */
    @Override
    public Optional<String> registerAccount(SignUpRequest request, String role) {
        if (userRepository.findByEmail(request.customerEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with customerEmail: " + request.customerEmail());
        }
        log.info("Register account request: {}", request);

        if (UserRole.ROLE_SELLER.name().equals(role)) {
            return Optional.of(registerSeller(request));
        } else if (UserRole.ROLE_CUSTOMER.name().equals(role)) {
            return Optional.of(registerUser(request));
        } else {
            throw new IllegalArgumentException("Unsupported role: " + role);
        }
    }

    /**
     * Handles the registration process for a new customer account.
     * <p>
     * This includes saving the customer to the database, assigning
     * ROLE_CUSTOMER, creating a shopping cart, and generating a JWT token.
     * </p>
     *
     * @param request the {@link SignUpRequest} containing user details
     * @return the generated JWT token for the new user
     */
    private String registerUser(SignUpRequest request) {
        User user = new User();
        user.setEmail(request.customerEmail());
        user.setFullName(request.customerFullName());
        user.setPhoneNumber(request.customerPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.customerPassword()));

        if (request.customerProfileImage() != null && !request.customerProfileImage().isEmpty()) {
            user.setImageUrl(request.customerProfileImage());
        }

        Authority authority = new Authority();
        authority.setRole(UserRole.ROLE_CUSTOMER);
        authority.setCustomer(user);
        user.getAuthorities().add(authority);

        userRepository.save(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return jwtService.generateToken(user);
    }

    /**
     * Handles the registration process for a new seller account.
     * <p>
     * This includes saving the seller to the database, assigning
     * ROLE_SELLER, creating business details, initializing a cart,
     * and generating a JWT token.
     * </p>
     *
     * @param request the {@link SignUpRequest} containing seller details
     * @return the generated JWT token for the new seller
     */
    private String registerSeller(SignUpRequest request) {
        Seller seller = new Seller();
        seller.setEmail(request.customerEmail());
        seller.setFullName(request.customerFullName());
        seller.setPhoneNumber(request.customerPhoneNumber());
        seller.setPassword(passwordEncoder.encode(request.customerPassword()));
        seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
//        seller.setSellerName("Seller " + request.customerFullName());

        if (request.customerProfileImage() != null && !request.customerProfileImage().isEmpty()) {
            seller.setImageUrl(request.customerProfileImage());
        }

        BusinessDetails details = new BusinessDetails();
        details.setBusinessName("My Business");
        details.setBusinessEmail("contact@mybusiness.com");
        details.setBusinessMobile("0123456789");
        details.setBusinessAddress("123 Street, City");
        details.setLogo("/images/logo.png");
        details.setBanner("/images/banner.png");
        seller.setBusinessDetails(details);

        Authority authority = new Authority();
        authority.setRole(UserRole.ROLE_SELLER);
        authority.setCustomer(seller);
        seller.getAuthorities().add(authority);

        sellerRepository.save(seller);

        Cart cart = new Cart();
        cart.setUser(seller);
        cartRepository.save(cart);

        return jwtService.generateToken(seller);
    }
}
