package com.ardent.commerce.strata.user.infrastructure.identity.keycloak;

import com.ardent.commerce.strata.shared.infrastructure.keycloak.KeycloakProperties;
import com.ardent.commerce.strata.user.domain.exception.IdentityProviderException;
import com.ardent.commerce.strata.user.domain.identity.UserIdentityService;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeycloakUserIdentityService implements UserIdentityService {
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    @Override
    public UUID createKeycloakUser(String email, String password) {
        return null;
    }

    @Override
    public void deleteKeycloakUser(UUID keycloakId) {

    }

    @Override
    public void updateKeycloakUserEmail(UUID keycloakId, String email) {

    }

    @Override
    public void updateKeycloakUserRole(UUID uuid, String role) {

    }

    @Override
    public void updateKeycloakUserPassword(UUID keycloakId, String newPassword) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newPassword);
        credential.setTemporary(false);

        try {
            keycloak.realm(keycloakProperties.getRealm())
                    .users()
                    .get(keycloakId.toString())
                    .resetPassword(credential);
        } catch (Exception e) {
            log.error("Failed to reset password in Keycloak for user: {}", keycloakId);
            throw new IdentityProviderException("Could not update user password in keycloak");
        }
    }

    @Override
    public void sendPasswordResetEmail(UUID keycloakId) {
        log.info("Triggering password reset, sending instruction for: {}", keycloakId);

        try {
            String redirectUrl = "login page url";

            // Keycloak handles the rest:
            // 1. Generates the token internally
            // 2. Sends the email
            // 3. Provides UI for password reset
            keycloak.realm(keycloakProperties.getRealm())
                    .users()
                    .get(keycloakId.toString())
                    .executeActionsEmail(
                            keycloakProperties.getClientId(),
                            redirectUrl,
                            List.of("UPDATE_PASSWORD")
                    );

            log.info("Password reset email sent to user: {}", keycloakId);

        } catch (NotFoundException e) {
            log.warn("User {} found in DB but does not exist in Keycloak", keycloakId);
        } catch (Exception e) {
            log.error("Failed to sent password reset email to user: {}", keycloakId, e);
            throw new IdentityProviderException("Failed to sent password reset email", e);
        }
    }

    public void verifyUserCredentials(String email, String password) {
        try(Keycloak userClient = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .username(email)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build()) {

            userClient.tokenManager().getAccessToken();
        } catch (NotAuthorizedException e) {
            log.warn("Password verification failed for user: {}", email);
            throw new BadCredentialsException("Incorrect password");
        } catch (Exception ex) {
            log.error("Error connection keycloak during password verification", ex);
            throw new IdentityProviderException("Keycloak identity service is currently unavailable");
        }
    }

}
