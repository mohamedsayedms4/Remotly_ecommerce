package org.example.remotly_ecommerce.helper.seller.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("jwtSellerStrategy")
@RequiredArgsConstructor
@Slf4j
public class JwtSellerStrategy implements SellerRetrievalStrategy{
    private final JwtUtil jwtUtil;
    private final SellerRepository sellerRepository;
    /**
     * @return
     * @throws SellerException
     */
    @Override
    public Optional<Seller> getSeller(String jwt) throws SellerException {
        try {
            String email = jwtUtil.extractEmailFromJwt(jwt);
            return sellerRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("Failed to extract seller from JWT: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
