package com.ardent.commerce.strata.user.infrastructure.persistence;

import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.application.service.UserEventPublisher;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 5. Save
 * 6. Publish events
 * 7. Return response
 */

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class UserPersistenceHandler {
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;
    private final UserMapper userMapper;

    public UserResponse persist(User user) {
        userRepository.save(user);
        log.debug("Persisted User aggregate [id={}]", user.getId());
        publishEvents(user);
        return userMapper.toResponse(user);
    }

    private void publishEvents(User user) {
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();
    }

}
