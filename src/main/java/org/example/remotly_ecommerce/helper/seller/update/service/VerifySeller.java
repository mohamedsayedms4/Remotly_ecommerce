package org.example.remotly_ecommerce.helper.seller.update.service;

import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;

import java.util.Optional;

public interface VerifySeller {
    Optional<Seller> verifySeller(Long id, Boolean emailVerified) throws SellerException;
}
