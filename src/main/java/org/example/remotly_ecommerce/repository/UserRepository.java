package org.example.remotly_ecommerce.repository;

import org.example.remotly_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods for users.
 *
 * Includes a method to find a user by their email address.
 *
 * Example usage:
 * {@code User user = userRepository.findByEmail("user@example.com");}
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email of the user to search for
     * @return the User object if found, or null if not found
     */
    Optional<User> findByEmail(String email);
}
