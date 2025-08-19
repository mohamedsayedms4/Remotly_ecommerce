package org.example.remotly_ecommerce.service.user.helper.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.utilis.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("jwtUserEntityStrategy")
@RequiredArgsConstructor
@Slf4j
public class JwtUserEntityStrategy implements UserRetrievalStrategy<User> {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUser(String input) throws UserException {
        String email = jwtUtil.extractEmailFromJwt(input);
        return userRepository.findByEmail(email);
    }
}
