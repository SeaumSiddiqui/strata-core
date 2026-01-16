package com.ardent.commerce.strata.user.application.dto;

/**
 * Generic Response, Does not reveal email exists or not
 */
public record ForgotUserPasswordResponse(
        String message,
        String details) {
}
