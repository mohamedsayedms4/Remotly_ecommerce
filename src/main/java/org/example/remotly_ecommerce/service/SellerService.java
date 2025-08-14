package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.dto.SellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.response.LoginRequest;
import org.example.remotly_ecommerce.response.SignUpRequest;

import java.util.List;
import java.util.Optional;

public interface SellerService {

    Optional<Seller> getSellerProfile(String jwt);
    Optional<String> createSeller(SignUpRequest request);
    Optional<String> login(LoginRequest request);
    Optional<Seller> getSellerById(Long id) throws SellerException;
    Optional<Seller> getSellerByEmail(String email)  throws SellerException;
    Optional<Seller> updateSeller(Seller seller);
    Optional<Seller> verifySeller(Long id , Boolean EmailVerified);
    void deleteSeller(Seller seller);
    Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status);
    boolean isSeller(User user) ;
    List<Seller> getAllSellers();
    List<Seller> getAllSellers(AccountStatus status);
    void deleteSellerById(Long id);
    List<SellerDto> getSellersByBusinessName(String businessName);
}
