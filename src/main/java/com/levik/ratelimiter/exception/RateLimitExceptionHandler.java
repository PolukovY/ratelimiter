package com.levik.ratelimiter.exception;

import com.levik.ratelimiter.controller.dto.ApiErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitExceptionHandler {

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiErrorMessage> handleInvalidFieldsInValidJson(final RateLimitException rateLimitException,
                                                                          final HttpServletRequest request) {
        final ApiErrorMessage apiErrorMessage = rateLimitException.toApiErrorMessage(request.getRequestURI());
        logIncomingCallException(rateLimitException, apiErrorMessage);
        return new ResponseEntity<>(apiErrorMessage, HttpStatus.TOO_MANY_REQUESTS);
    }

    private static void logIncomingCallException(final RateLimitException rateLimitException,
                                                 final ApiErrorMessage apiErrorMessage) {
        log.error(String.format("%s: %s", apiErrorMessage.getId(), rateLimitException.getMessage()), rateLimitException);
    }
}
