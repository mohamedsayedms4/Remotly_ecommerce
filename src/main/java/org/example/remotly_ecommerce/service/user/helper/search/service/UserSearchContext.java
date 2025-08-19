package org.example.remotly_ecommerce.service.user.helper.search.service;

import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Context class for executing seller retrieval using different strategies.
 * <p>
 * This class uses the Strategy design pattern to support multiple ways
 * to retrieve a seller, such as by JWT, email, or ID.
 * It delegates the retrieval request to the appropriate {@link UserRetrievalStrategy} implementation.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Maintain a map of seller retrieval strategies.</li>
 *     <li>Execute retrieval using the selected strategy.</li>
 *     <li>Throw an exception if the requested strategy is unknown.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 1.0
 * @since 2025-08-16
 */


// تحديث الـ Context class
@Service
@RequiredArgsConstructor
public class UserSearchContext {

    private final Map<String, UserRetrievalStrategy<?>> strategies;

    /**
     * Execute retrieval with generic return type
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> execute(String type, String input, Class<T> returnType) throws UserException {
        UserRetrievalStrategy<?> strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown user retrieval strategy: " + type);
        }
        return (Optional<T>) strategy.getUser(input);
    }

    // Convenience methods with specific return types
    public Optional<UserFullInformationDto> executeForDto(String type, String input) throws UserException {
        return execute(type, input, UserFullInformationDto.class);
    }

    public Optional<User> executeForEntity(String type, String input) throws UserException {
        return execute(type, input, User.class);
    }
}
