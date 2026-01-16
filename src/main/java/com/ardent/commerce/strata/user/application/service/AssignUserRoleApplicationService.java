package com.ardent.commerce.strata.user.application.service;

import com.ardent.commerce.strata.shared.application.ApplicationService;
import com.ardent.commerce.strata.user.application.command.AssignUserRoleCommand;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class AssignUserRoleApplicationService implements ApplicationService<AssignUserRoleCommand, UserResponse> {
    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse execute(AssignUserRoleCommand command) {
        log.info("Assigning new role {} to user: {}", command.roleName(), command.userId());

        User user = userDomainService.fetchActiveByUserId(command.userId());
        UserRole roleToAdd = UserRole.from(command.roleName());

        user.assignRole(roleToAdd);
        userRepository.save(user);
        log.info("Successfully assigned new role {} to user: {}", command.roleName(), command.userId());

        user.getDomainEvents().forEach(userEventPublisher::publish);
        user.clearDomainEvents();

        return userMapper.toResponse(user);
    }

}
