package org.example.remotly_ecommerce.helper.seller.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component("emailSellerStrategy")
@RequiredArgsConstructor
@Slf4j
public class EmailSellerStrategy implements SellerRetrievalStrategy {

    private final SellerRepository sellerRepository;

    /**
     * Retrieve a seller by email.
     *
     * @param email the seller's email
     * @return Optional<Seller> if found
     * @throws SellerException if seller not found or email is invalid
     */
    @Override
    public Optional<Seller> getSeller(String email) throws SellerException {

            log.info("Searching for seller with customerEmail: {}", email);

            Optional<Seller> seller = sellerRepository.findByEmail(email);

            if (seller.isEmpty()) {
                log.error("Seller not found for customerEmail: {}", email);
                throw new SellerException("Seller not found with email: " + email);
            }

            log.debug("Seller found: {}", seller);
            return seller;

//         catch (Exception e) {
//            log.error("Error occurred while retrieving seller by email {}: {}", email, e.getMessage());
//            throw new SellerException("Failed to retrieve seller with email: " + email);
//        }
    }
}
