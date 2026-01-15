package com.ardent.commerce.strata.user.infrastructure.controller;

import com.ardent.commerce.strata.user.application.dto.ForgotPasswordRequest;
import com.ardent.commerce.strata.user.application.dto.ForgotPasswordResponse;
import com.ardent.commerce.strata.user.application.service.ForgotPasswordApplicationService;
import com.ardent.commerce.strata.user.application.service.ResetPasswordApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final ForgotPasswordApplicationService forgotPasswordService;
    private final ResetPasswordApplicationService resetPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("POST /api/auth/forgot-password - forgot password request");

        ForgotPasswordResponse response = forgotPasswordService.execute(request);
        return ResponseEntity.ok(response);
    }
}
