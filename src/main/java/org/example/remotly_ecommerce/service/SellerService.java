package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.model.Seller;

import java.util.Optional;

public interface SellerService {

    Optional<Seller> getSeller(String jwt);
    Optional<Seller> createSeller(Seller seller);
    Optional<Seller> getSellerById(Long id);
    Optional<Seller> updateSeller(Seller seller);
    void deleteSeller(Seller seller);
    Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status);
}
