package com.ardent.commerce.strata.user.infrastructure.exception;

import com.ardent.commerce.strata.shared.infrastructure.exception.ErrorResponse;
import com.ardent.commerce.strata.user.domain.exception.DuplicateEmailException;
import com.ardent.commerce.strata.user.domain.exception.InvalidEmailException;
import com.ardent.commerce.strata.user.domain.exception.InvalidPhoneException;
import com.ardent.commerce.strata.user.domain.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * User-specific exception handler.
 * Handles all USER context domain exceptions.

 * basePackages = Only catches exceptions from user controllers
 * @Order(LOWEST_PRECEDENCE) = Global handler fires first, then this one
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.ardent.commerce.strata.user.infrastructure.controller")
@Order(LOWEST_PRECEDENCE)
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

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<?> handleDuplicateEmailException(DuplicateEmailException ex) {
        log.warn("Email already registered: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "Duplicate email",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<?> handleInvalidEmailFormat(InvalidEmailException ex) {
        log.warn("Invalid email address {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid email address",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidPhoneException.class)
    public ResponseEntity<?> handleInvalidPhoneException(InvalidPhoneException ex) {
        log.warn("Invalid phone number {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid phone number",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}
