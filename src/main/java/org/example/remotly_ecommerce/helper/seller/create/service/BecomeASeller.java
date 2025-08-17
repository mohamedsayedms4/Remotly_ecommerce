package org.example.remotly_ecommerce.helper.seller.create.service;

import org.example.remotly_ecommerce.dto.BecomeASellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;

import java.util.Optional;

public interface BecomeASeller {
    Optional<Seller> becomeASeller(Long id , BecomeASellerDto becomeASellerDto ) throws SellerException;

}
