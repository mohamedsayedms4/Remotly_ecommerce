package org.example.remotly_ecommerce.service.user.helper.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.mapper.UserMapper;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component("emailUserDtoStrategy")
@RequiredArgsConstructor
@Slf4j
public class EmailUserStrategy implements UserRetrievalStrategy {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    /**
     * @param input
     * @return
     * @throws UserException
     */
    @Override
    public Optional<UserFullInformationDto> getUser(String input) throws UserException {
        log.info("Searching for user with user Email: {}", input);
        Optional<UserFullInformationDto> user = userRepository.findByEmail(input)
                .map(userMapper::toUserFullInformationDtoDto);
        if (user.isEmpty()) {
            log.error("User not found with user Email: {}", input);
            throw new UserException("User not found with user Email: " + input);
        }
        log.debug("User found: {}", user);
        return user;
    }
}
