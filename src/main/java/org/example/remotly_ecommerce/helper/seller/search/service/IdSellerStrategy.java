package org.example.remotly_ecommerce.helper.seller.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("idSellerStrategy")
@RequiredArgsConstructor
@Slf4j
public class IdSellerStrategy implements SellerRetrievalStrategy {
    private final SellerRepository sellerRepository;

    /**
     * @param input the seller ID as a String
     * @return Optional<Seller>
     * @throws SellerException if seller not found or invalid ID
     */
    @Override
    public Optional<Seller> getSeller(String input) throws SellerException {

            Long id = Long.valueOf(input);  // تحويل الـ String إلى Long
            log.info("Searching for seller with id: {}", id);
            Optional<Seller> seller = sellerRepository.findById(id);
            if (seller.isEmpty()) {
                log.error("Seller not found for id: {}", id);
                throw new SellerException("Seller not found with ID: " + id);
            }
            log.debug("Seller found: {}", seller);
            return seller;
//        } catch (NumberFormatException e) {
//            log.error("Invalid ID format: {}", input);
//            throw new SellerException("Invalid ID format: " + input);
//        }
    }
}
