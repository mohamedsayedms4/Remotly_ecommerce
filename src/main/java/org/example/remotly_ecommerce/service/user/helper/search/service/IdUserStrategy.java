package org.example.remotly_ecommerce.service.user.helper.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.user.UserFullInformationDto;
import org.example.remotly_ecommerce.exception.UserException;
import org.example.remotly_ecommerce.mapper.UserMapper;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("idUserDtoStrategy")
@RequiredArgsConstructor
@Slf4j
public class IdUserStrategy implements UserRetrievalStrategy {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    /**
     * @param input
     * @return
     * @throws UserException
     */
    @Override
    public Optional<UserFullInformationDto> getUser(String input) throws UserException {
        Long id = Long.valueOf(input);
        log.info("Searching for user with id: {}", id);
        Optional<UserFullInformationDto> user = userRepository.findById(id)
                .map(userMapper::toUserFullInformationDtoDto);
        if (user.isEmpty()) {
            log.error("User not found for id: {}", id);
            String errorMessage = messageSource.getMessage(
                    "error.user.notfound.id",
                    new Object[]{id},
                    LocaleContextHolder.getLocale()
            );
            throw new UserException(errorMessage);
        }
        log.debug("User found: {}", user);


        return user;
    }
}
