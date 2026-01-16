package com.ardent.commerce.strata.user.application.command;

import java.util.UUID;

public record RemoveUserRoleCommand(
        UUID userId,
        String roleName) {
}
