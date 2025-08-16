package org.example.remotly_ecommerce.repository;

import org.example.remotly_ecommerce.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link VerificationCode} entity.
 * Provides CRUD operations and custom queries related to verification codes.
 *
 * Includes a method to find a verification code entry by customerEmail address.
 *
 * Example usage:
 * {@code VerificationCode code = verificationCodeRepository.findByEmail("example@example.com");}
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    /**
     * Finds a {@link VerificationCode} by the provided customerEmail.
     *
     * @param email the customerEmail associated with the verification code
     * @return the VerificationCode object if found, or null if not found
     */
    VerificationCode findByEmail(String email);
}
