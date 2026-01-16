package com.ardent.commerce.strata.user.application.command;

import java.util.UUID;

public record UpdateUserCommand(
        UUID keycloakId,
        String firstName,
        String lastName,
        String phone) {
}
