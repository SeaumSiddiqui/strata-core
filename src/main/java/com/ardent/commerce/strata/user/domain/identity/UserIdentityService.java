package com.ardent.commerce.strata.user.domain.identity;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public interface UserIdentityService {

    UUID createKeycloakUser(String email, String password);

    void deleteKeycloakUser(UUID keycloakId);

    void updateKeycloakUserEmail(UUID keycloakId, String email);

    void updateKeycloakUserRole(UUID uuid, String role);

    void updateKeycloakUserPassword(UUID keycloakId, String s);
}
