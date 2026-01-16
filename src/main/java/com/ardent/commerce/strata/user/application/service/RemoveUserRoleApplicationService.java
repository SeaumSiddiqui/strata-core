package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.RemoveUserRoleCommand;
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

/**
 * Application Service: Manage user roles.
 * Default role - CUSTOMER assigned on creation, nonremovable.
 * Role ADMIN, STAFF can be assigned or removed.

 * Steps:
 * 1. Load aggregate with command userId
 * 2. Assign/Remove role
 * 3. Save
 * 4. Publish events
 * 5. Return response
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RemoveUserRoleApplicationService implements ApplicationService<RemoveUserRoleCommand, UserResponse> {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse execute(RemoveUserRoleCommand command) {
        log.info("Removing existing role {} from user: {}", command.roleName(), command.userId());

        User user = userDomainService.fetchActiveByUserId(command.userId());
        UserRole roleToRemove = UserRole.from(command.roleName());

        user.removeRole(roleToRemove);
        userRepository.save(user);
        log.info("Successfully removed role {} from user: {}", command.roleName(), command.userId());

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

}
