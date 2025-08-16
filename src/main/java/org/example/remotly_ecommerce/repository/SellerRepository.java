package org.example.remotly_ecommerce.repository;


import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.model.BusinessDetails;
import org.example.remotly_ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Seller} entity.
 * Provides basic CRUD operations and custom query methods.
 *
 * Includes a method to find a seller by their customerEmail address.
 *
 * Example usage:
 * {@code Seller seller = sellerRepository.findByEmail("example@example.com");}
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    /**
     * Finds a seller by their customerEmail address.
     *
     * @param email the customerEmail of the seller to search for
     * @return the Seller object if found, or null if not found
     */
    Optional<Seller> findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
    List<Seller> findByBusinessDetails_BusinessNameContainingIgnoreCase(String partialName);

}
