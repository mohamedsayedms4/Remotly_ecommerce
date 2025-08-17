package org.example.remotly_ecommerce.helper.seller.search.service;

import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Context class for executing seller retrieval using different strategies.
 * <p>
 * This class uses the Strategy design pattern to support multiple ways
 * to retrieve a seller, such as by JWT, email, or ID.
 * It delegates the retrieval request to the appropriate {@link SellerRetrievalStrategy} implementation.
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
@Service
@RequiredArgsConstructor
public class SellerSearchContext {

    private final Map<String, SellerRetrievalStrategy> strategies;

    /**
     * Executes seller retrieval using the specified strategy type.
     *
     * @param type  the strategy name (e.g., "jwtSellerStrategy", "emailSellerStrategy", "idSellerStrategy")
     * @param input the input value required by the strategy (jwt, email, or id as String)
     * @return an {@link Optional} of {@link Seller} if found
     * @throws SellerException if the strategy fails to retrieve the seller
     * @throws IllegalArgumentException if the strategy type is unknown
     */
    public Optional<Seller> execute(String type, String input) throws SellerException {
        SellerRetrievalStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown seller retrieval strategy: " + type);
        }
        return strategy.getSeller(input);
    }
}
