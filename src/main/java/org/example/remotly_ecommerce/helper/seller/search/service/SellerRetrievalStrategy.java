package org.example.remotly_ecommerce.helper.seller.search.service;

import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;

import java.util.Optional;

public interface SellerRetrievalStrategy {
    Optional<Seller> getSeller(String input) throws SellerException;
}
