package org.example.remotly_ecommerce.repository;


import org.example.remotly_ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Seller} entity.
 * Provides basic CRUD operations and custom query methods.
 *
 * Includes a method to find a seller by their email address.
 *
 * Example usage:
 * {@code Seller seller = sellerRepository.findByEmail("example@example.com");}
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
public interface SellerRepository extends JpaRepository<Seller, Long> {

    /**
     * Finds a seller by their email address.
     *
     * @param email the email of the seller to search for
     * @return the Seller object if found, or null if not found
     */
    Seller findByEmail(String email);
}
