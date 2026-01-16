package com.ardent.commerce.strata.user.application.command;

import java.util.UUID;

public record DeleteUserCommand(
        UUID keycloakId) {
}
