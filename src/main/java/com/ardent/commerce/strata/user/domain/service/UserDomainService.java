package com.ardent.commerce.strata.user.domain.service;

import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Domain Service: Stateless business logic.
 * Handles operations that require knowledge of entire user collection.
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDomainService {
    private final UserRepository userRepository;

    /**
     * Business rule: Change user email
     */
    public void changeEmail(User user, Email newEmail) {
        log.info("Attempting to change email for user {} to {}", user.getId(), newEmail.value());

        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already registered: " + newEmail.value());
        }
        user.changeEmail(newEmail);
        userRepository.save(user);

        log.info("Email successfully changed for user {}", user.getId());
    }
}
