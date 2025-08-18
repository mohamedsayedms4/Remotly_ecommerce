package org.example.remotly_ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.model.BlacklistedToken;
import org.example.remotly_ecommerce.repository.BlacklistedTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl {
    private final BlacklistedTokenRepository repository;

    public void blacklistToken(String token) {
        if (!repository.existsByToken(token)) {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            blacklistedToken.setBlacklistedAt(LocalDateTime.now());
            repository.save(blacklistedToken);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return repository.existsByToken(token);
    }
}
