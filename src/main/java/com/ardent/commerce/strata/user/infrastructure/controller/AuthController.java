package com.ardent.commerce.strata.user.infrastructure.controller;

import com.ardent.commerce.strata.user.application.command.CreateUserCommand;
import com.ardent.commerce.strata.user.application.command.ForgotUserPasswordCommand;
import com.ardent.commerce.strata.user.application.dto.CreateUserRequest;
import com.ardent.commerce.strata.user.application.dto.ForgotUserPasswordRequest;
import com.ardent.commerce.strata.user.application.dto.ForgotUserPasswordResponse;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.application.service.CreateUserApplicationService;
import com.ardent.commerce.strata.user.application.service.ForgotUserPasswordApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.hash;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final CreateUserApplicationService createUserService;
    private final ForgotUserPasswordApplicationService forgotPasswordService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/auth/register - Creating user with email hash: {}", hash(request.email()));

        CreateUserCommand command = new CreateUserCommand(
                request.email(),
                request.phone(),
                request.firstName(),
                request.lastName(),
                request.roles(),
                request.password());

        UserResponse response = createUserService.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotUserPasswordResponse> forgotPassword(@Valid @RequestBody ForgotUserPasswordRequest request) {
        log.info("POST /api/auth/forgot-password - forgot password request");

        ForgotUserPasswordCommand command = new ForgotUserPasswordCommand(request.email());

        ForgotUserPasswordResponse response = forgotPasswordService.execute(command);
        return ResponseEntity.ok(response);
    }

}
