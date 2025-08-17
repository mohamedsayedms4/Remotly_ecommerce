package org.example.remotly_ecommerce.helper.seller.update.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Component
@Slf4j
public class UpdateSellerAccountStatusImpl implements UpdateSellerAccountStatus {
    private final SellerRepository sellerRepository;
    @Override
    public Optional<Seller> updateSellerAccountStatus(Long id, AccountStatus status)
            throws SellerException {

        Optional<Seller> sellerOpt = sellerRepository.findById(id);
        if (sellerOpt.isEmpty()) {
            log.error("Seller not found");
            throw new SellerException("Seller not found");
        }
        if (status != AccountStatus.ACTIVE &&
                status != AccountStatus.SUSPENDED &&
                status != AccountStatus.BANNED &&
                status != AccountStatus.CLOSED &&
                status != AccountStatus.PENDING_VERIFICATION &&
                status != AccountStatus.DEACTIVATED) {

            log.error("Invalid account status: {}", status);
            throw new SellerException("Invalid account status: " + status);
        }

        Seller seller = sellerOpt.get();
        seller.setAccountStatus(status);
        sellerRepository.save(seller);
        return Optional.of(seller);

    }
}
