package com.ardent.commerce.strata.user.infrastructure.exception;

import com.ardent.commerce.strata.shared.infrastructure.exception.ErrorResponse;
import com.ardent.commerce.strata.user.domain.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * User Exception Handler: Catches and formats all user module exceptions.
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.ardent.commerce.strata.user")
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){
        log.warn("User not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "User not found",
                        ex.getMessage(),
                        LocalDateTime.now()

                ));

    }
}
