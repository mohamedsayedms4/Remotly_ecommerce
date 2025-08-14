package org.example.remotly_ecommerce.repository;


import org.example.remotly_ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Cart} entity.
 * Provides basic CRUD operations via Spring Data JPA.
 *
 * This interface allows you to perform operations such as save, find, delete on the Cart table.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
