package org.example.remotly_ecommerce.helper.seller.create.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.AccountStatus;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.BecomeASellerDto;
import org.example.remotly_ecommerce.exception.SellerException;
import org.example.remotly_ecommerce.model.Authority;
import org.example.remotly_ecommerce.model.BusinessDetails;
import org.example.remotly_ecommerce.model.Seller;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.SellerRepository;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BecomeASellerImpl implements BecomeASeller {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public Optional<Seller> becomeASeller(Long id, BecomeASellerDto dto) throws SellerException {
        log.info("Start becomeASeller process for id {} with dto {}", id, dto.toString());

        // جيب اليوزر
        User user = userRepository.findById(id)
                .orElseThrow(() -> new SellerException("User not found with id: " + id));
        log.info("Fetched user with id: {}", user.getId());

        // تأكد إنه مش seller بالفعل
        boolean alreadySeller = user.getAuthorities().stream()
                .anyMatch(auth -> auth.getRole() == UserRole.ROLE_SELLER);
        log.info("Is user already seller? {}", alreadySeller);

        if (alreadySeller) {
            log.warn("User {} is already a seller", id);
            throw new SellerException("User is already a seller");
        }

        // احفظ بيانات اليوزر قبل الحذف
        String userEmail = user.getEmail();
        String userFullName = user.getFullName();
        String userPhoneNumber = user.getPhoneNumber();
        String userPassword = user.getPassword();
        String userImageUrl = user.getImageUrl();
        var userPickupAddress = user.getPickupAddress();
        var userCart = user.getCart();
        var userAuthorities = new HashSet<>(user.getAuthorities()); // نسخة منفصلة
        var userUsedCoupons = new HashSet<>(user.getUsedCoupons()); // نسخة منفصلة
        log.info("Saved user data before deletion");

        // فك العلاقات قبل الحذف
        if (userCart != null) {
            userCart.setUser(null); // فك علاقة Cart
        }

        // امسح الـ authorities الخاصة بالـ user
        user.getAuthorities().clear();
        user.getUsedCoupons().clear();

        // احفظ التغييرات قبل الحذف
        userRepository.save(user);
        userRepository.flush();

        // تحويل User إلى Seller
        log.info("Converting user {} to seller...", id);

        // احذف اليوزر الحالي
        userRepository.deleteById(id);
        userRepository.flush(); // تأكد من حصول الحذف فعلياً

        // إنشاء Seller جديد
        Seller seller = new Seller();

        // استخدم نفس الـ ID للـ Seller الجديد
        seller.setId(id);
        seller.setEmail(userEmail);
        log.info("Set email = {}", userEmail);

        seller.setFullName(userFullName);
        log.info("Set fullName = {}", userFullName);

        seller.setPhoneNumber(userPhoneNumber);
        log.info("Set phoneNumber = {}", userPhoneNumber);

        seller.setPassword(userPassword);
        log.info("Set password (hidden)");

        seller.setImageUrl(userImageUrl);
        log.info("Set imageUrl = {}", userImageUrl);

        seller.setPickupAddress(userPickupAddress);
        log.info("Set pickupAddress = {}", userPickupAddress);

        // حفظ Cart إذا كان موجود وربطه بالـ Seller الجديد
        if (userCart != null) {
            userCart.setUser(seller); // ربط Cart بالـ Seller الجديد
            seller.setCart(userCart);
            log.info("Copied cart from original user");
        }

        // حفظ الكوبونات المستخدمة
        seller.getUsedCoupons().addAll(userUsedCoupons);

        // إنشاء authorities جديدة للـ Seller (ما عدا الـ CUSTOMER authorities القديمة)
        for (var authority : userAuthorities) {
            if (authority.getRole() != UserRole.ROLE_CUSTOMER) {
                Authority newAuth = new Authority();
                newAuth.setRole(authority.getRole());
                newAuth.setCustomer(seller);
                seller.getAuthorities().add(newAuth);
            }
        }

        // أضف authority جديدة للـ SELLER
        Authority sellerAuth = new Authority();
        sellerAuth.setRole(UserRole.ROLE_SELLER);
        sellerAuth.setCustomer(seller);
        seller.getAuthorities().add(sellerAuth);
        log.info("Added SELLER authority to seller authorities");

        // إنشاء بيانات البيزنس
        BusinessDetails details = new BusinessDetails();
        details.setBusinessName(dto.businessName());
        log.info("Set businessName = {}", dto.businessName());

        details.setBusinessEmail(dto.businessEmail());
        log.info("Set businessEmail = {}", dto.businessEmail());

        details.setBusinessMobile(dto.businessMobile());
        log.info("Set businessMobile = {}", dto.businessMobile());

        details.setBusinessAddress(dto.businessAddress());
        log.info("Set businessAddress = {}", dto.businessAddress());

        details.setLogo(dto.logo());
        log.info("Set logo = {}", dto.logo());

        details.setBanner(dto.banner());
        log.info("Set banner = {}", dto.banner());

        seller.setBusinessDetails(details);
        log.info("Assigned BusinessDetails to seller");

        seller.setIsEmailVerified(false);
        log.info("Set isEmailVerified = false");

        seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        log.info("Set accountStatus = PENDING_VERIFICATION");

        // احفظ الـ Seller الجديد
        Seller savedSeller = sellerRepository.save(seller);
        log.info("Saved seller successfully with id {} and email {}", savedSeller.getId(), savedSeller.getEmail());

        return Optional.of(savedSeller);
    }
}