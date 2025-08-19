package org.example.remotly_ecommerce.service.user.helper.search.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.UserException;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractUserRetrievalStrategy<T> implements UserRetrievalStrategy<T> {

    protected final Class<T> returnType;
    protected final String searchType;

    // التحقق من صحة المدخلات
    protected void validateInput(String input) throws UserException {
        if (input == null || input.trim().isEmpty()) {
            throw new UserException("Input parameter cannot be null or empty");
        }
    }

    // تسجيل العمليات
    protected void logSearchOperation(String input) {
        log.info("Searching for user using {} strategy with input: {}", searchType, input);
    }
}