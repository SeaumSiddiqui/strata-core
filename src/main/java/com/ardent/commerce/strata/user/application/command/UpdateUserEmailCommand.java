package com.ardent.commerce.strata.user.application.command;

import java.util.UUID;

public record UpdateUserEmailCommand(
        UUID keycloakId,
        String newEmail,
        String password) {
}
