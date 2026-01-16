package com.ardent.commerce.strata.user.infrastructure.controller;

import com.ardent.commerce.strata.user.application.command.AssignUserRoleCommand;
import com.ardent.commerce.strata.user.application.command.RemoveUserRoleCommand;
import com.ardent.commerce.strata.user.application.dto.UpdateUserRoleRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.application.service.AssignUserRoleApplicationService;
import com.ardent.commerce.strata.user.application.service.FindUserApplicationService;
import com.ardent.commerce.strata.user.application.service.RemoveUserRoleApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/admin/users")
@RestController
public class UserAdminController {
    private final FindUserApplicationService findUserService;
    private final AssignUserRoleApplicationService assignUserRoleService;
    private final RemoveUserRoleApplicationService removeUserRoleService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        log.info("GET /api/admin/users/{} - Fetching user by ID", userId);

        UserResponse response = findUserService.findById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/role/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignUserRole(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRoleRequest request) {
        log.info("PUT /api/admin/users/userId/role/assign - Assign new role to user");

        AssignUserRoleCommand command = new AssignUserRoleCommand(userId, request.roleName());
        UserResponse response = assignUserRoleService.execute(command);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/role/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUserRole(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRoleRequest request) {
        log.info("PUT /api/admin/users/userId/role/remove - Remove and existing role from user");

        RemoveUserRoleCommand command = new RemoveUserRoleCommand(userId, request.roleName());
        UserResponse response = removeUserRoleService.execute(command);
        return ResponseEntity.ok(response);
    }

}
