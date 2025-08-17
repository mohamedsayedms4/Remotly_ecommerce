package org.example.remotly_ecommerce.helper.seller.update.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.helper.seller.search.service.SellerSearchContext;
import org.example.remotly_ecommerce.helper.user.sign_in.LoginContext;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VerifySellerImpl implements VerifySeller {
    private final SellerSearchContext  sellerSearchContext;
    private final SellerRepository sellerRepository;
    /**
     * @param id
     * @param emailVerified
     * @return
     */
    @Override
    public Optional<Seller> verifySeller(Long id, Boolean emailVerified) throws SellerException {
        String strID = Long.toString(id);
        Optional<Seller> sellerOpt = sellerSearchContext.execute("idSellerStrategy",strID);

        if (sellerOpt.isEmpty()){
            log.error("Seller not found");
            throw new SellerException("Seller not found");
        }
        if(emailVerified != true && emailVerified != false){
            log.error("Invalid account emailVerified: {}", emailVerified);
            throw new SellerException("Invalid account emailVerified: " + emailVerified);
        }
        Seller seller = sellerOpt.get();
        seller.setIsEmailVerified(emailVerified);
        sellerRepository.save(seller);
        return Optional.of(seller);
    }
}
