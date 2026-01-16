package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.ForgotUserPasswordCommand;
import com.ardent.commerce.strata.user.application.dto.ForgotUserPasswordResponse;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.identity.keycloak.KeycloakUserIdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ForgotUserPasswordApplicationService implements ApplicationService<ForgotUserPasswordCommand, ForgotUserPasswordResponse> {
    private final UserDomainService userDomainService;
    private final KeycloakUserIdentityService keycloakUserIdentityService;

    @Override
    public ForgotUserPasswordResponse execute(ForgotUserPasswordCommand command) {
        String email = command.email().toLowerCase().trim();
        log.info("Forgot password request for email: {}", email);

        try {
            userDomainService.findByEmail(email).ifPresent(
                    user -> keycloakUserIdentityService.sendPasswordResetEmail(user.getKeycloakId()));

        } catch (Exception e) {
            log.error("Silent failure for forgot password request: {}. Error: {}", email, e.getMessage(), e);
        }

        return new ForgotUserPasswordResponse(
                "Password reset instructions sent to email",
                "If an account exists with this email, you will receive password reset instructions"
        );
    }

}
