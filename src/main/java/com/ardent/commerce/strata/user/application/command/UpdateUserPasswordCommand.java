package com.ardent.commerce.strata.user.application.command;

import java.util.UUID;

public record UpdateUserPasswordCommand(
        UUID keycloakId,
        String currentPassword,
        String newPassword) {
}
