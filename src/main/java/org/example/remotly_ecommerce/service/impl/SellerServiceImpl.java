package org.example.remotly_ecommerce.service.impl;

import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.service.SellerService;

import java.util.Optional;

public class SellerServiceImpl implements SellerService {
    /**
     * @param jwt
     * @return
     */
    @Override
    public Optional<Seller> getSeller(String jwt) {
        return Optional.empty();
    }

    /**
     * @param seller
     * @return
     */
    @Override
    public Optional<Seller> createSeller(Seller seller) {
        return Optional.empty();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<Seller> getSellerById(Long id) {
        return Optional.empty();
    }

    /**
     * @param seller
     * @return
     */
    @Override
    public Optional<Seller> updateSeller(Seller seller) {
        return Optional.empty();
    }

    /**
     * @param seller
     */
    @Override
    public void deleteSeller(Seller seller) {

    }

    /**
     * @param id
     * @param status
     * @return
     */
    @Override
    public Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status) {
        return Optional.empty();
    }
}
