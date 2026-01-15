package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.user.application.dto.UpdateUserRoleRequest;
import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserRole;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Application Service: Manage user roles.
 * Default role - CUSTOMER assigned on creation, nonremovable.
 * Role ADMIN, STAFF can be assigned or removed.

 * Steps:
 * 1. Load aggregate with userId
 * 2. Assign/Remove role
 * 3. Save
 * 4. Publish events
 * 5. Return response
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ManageUserRoleApplicationService {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse assignUserRole(UUID userId, UpdateUserRoleRequest request) {
        log.info("Assigning new role {} to user: {}", request.roleName(), userId);

        User user = userDomainService.fetchActiveByUserId(userId);
        UserRole roleToAdd = UserRole.from(request.roleName());

        user.assignRole(roleToAdd);
        userRepository.save(user);
        log.info("Successfully assigned new role {} to user: {}", request.roleName(), userId);

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse removeUserRole(UUID userId, UpdateUserRoleRequest request) {
        log.info("Removing existing role {} from user: {}", request.roleName(), userId);

        User user = userDomainService.fetchActiveByUserId(userId);
        UserRole roleToRemove = UserRole.from(request.roleName());

        user.removeRole(roleToRemove);
        userRepository.save(user);
        log.info("Successfully removed role {} from user: {}", request.roleName(), userId);

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

}
