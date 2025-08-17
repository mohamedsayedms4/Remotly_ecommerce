package org.example.remotly_ecommerce.helper.seller.update.service;

import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;

import java.util.Optional;

public interface UpdateSellerAccountStatus {

    Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status) throws SellerException;
}
