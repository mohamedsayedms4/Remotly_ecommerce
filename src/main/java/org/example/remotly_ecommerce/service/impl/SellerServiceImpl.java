package org.example.remotly_ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.InvalidPWD;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.exception.UserAlreadyExistsException;
import org.example.remotly_ecommerce.mapper.SellerMapper;
import org.example.remotly_ecommerce.model.*;
import org.example.remotly_ecommerce.repository.CartRepository;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.response.LoginRequest;
import org.example.remotly_ecommerce.response.SignUpRequest;
import org.example.remotly_ecommerce.service.SellerService;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final JwtUtil jwtUtil ;
    private final SellerMapper sellerMapper;



    /**
     * @param jwt
     * @return
     */
    @Override
    public Optional<Seller> getSellerProfile(String jwt) {
        try {
            String email = jwtUtil.extractEmailFromJwt(jwt);
            return sellerRepository.findByEmail(email);
        } catch (Exception e) {
            System.out.println("Failed to extract seller from JWT: " + e.getMessage());
            return Optional.empty();
        }
    }


    /**
     * @param request
     * @return
     */
    @Override
    public Optional<String> createSeller(SignUpRequest request) {
        if (sellerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        Seller seller = new Seller();
        seller.setEmail(request.getEmail());
        seller.setFullName(request.getFullName());
        seller.setPhoneNumber(request.getPhoneNumber());
        seller.setPassword(passwordEncoder.encode(request.getPassword()));
        seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        seller.setSellerName("Seller jjjjj");

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

        Seller savedSeller = sellerRepository.save(seller);

        Cart cart = new Cart();
        cart.setUser(savedSeller);
        cartRepository.save(cart);

        String jwtToken = jwtService.generateToken(savedSeller);

        return Optional.of(jwtToken);
    }

    /**
     * @param request
     * @return
     */
    @Override
    public Optional<String> login(LoginRequest request) {
        Optional<Seller> sellerOpt = sellerRepository.findByEmail(request.getEmail());

        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get(); // استخراج الكائن من Optional

            if (!passwordEncoder.matches(request.getPassword(), seller.getPassword())) {
                throw new InvalidPWD("Invalid password");
            }

            String jwtToken = jwtService.generateToken(seller);
            UserRole role = seller.getAuthorities().iterator().next().getRole();

            return Optional.of(jwtToken); // إرجاع التوكن
        }

        return Optional.empty(); // لو المستخدم مش موجود
    }



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
     * @param email
     * @return
     */
    @Override
    public Optional<Seller> getSellerByEmail(String email) throws SellerException {
        log.info("Searching for seller with email: {}", email);

        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isEmpty()) {
            log.error("Seller not found for email: {}", email);

            throw new SellerException("Seller not found with email: " + email);
        }
        log.debug("Seller found: {}", seller);

        return seller;
    }


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
     * @param id
     * @return
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


    @Override
    @Transactional
    public void deleteSeller(Seller seller) {
        if (seller.getId() != null) {
            sellerRepository.deleteById(seller.getId());
        }
    }

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
     * @param businessName
     * @return
     */
    @Override
    public List<SellerDto> getSellersByBusinessName(String businessName) {
        if (businessName == null || businessName.isEmpty()) {
            return Collections.emptyList();
        }
        List<Seller> sellers =  sellerRepository.findByBusinessDetails_BusinessNameContainingIgnoreCase(businessName);
        return sellers.stream()
                .filter(s -> s.getAccountStatus() == AccountStatus.ACTIVE)
                .map(sellerMapper::toDto)
                .collect(Collectors.toList());

    }


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
     * @param user
     * @return
     */
    @Override
    public boolean isSeller(User user) {

        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
       return sellerRepository.findByAccountStatus(status);
    }

}
