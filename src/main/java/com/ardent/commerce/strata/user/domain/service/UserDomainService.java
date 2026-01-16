package com.ardent.commerce.strata.user.domain.service;

import com.ardent.commerce.strata.user.domain.exception.DuplicateEmailException;
import com.ardent.commerce.strata.user.domain.exception.UserInactiveException;
import com.ardent.commerce.strata.user.domain.exception.UserNotFoundException;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserId;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain Service: Stateless business logic.
 * Handles operations that require knowledge of entire user collection.
 */

@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    public User fetchActiveByUserId(UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(()-> new UserNotFoundException(userId));

        if (!user.isActive()) {
            throw new UserInactiveException(user.getKeycloakId());
        }
        return user;
    }

    public User fetchActiveByKeycloakId(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(()-> UserNotFoundException.byKeycloakId(keycloakId));

        if (!user.isActive()) {
            throw new UserInactiveException(keycloakId);
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(Email.of(email))
                .filter(User::isActive); // Only returns if the user is active
    }

    public void assertEmailIsUnique(Email newEmail) {
        if (userRepository.existsByEmail(newEmail)) {
            throw new DuplicateEmailException(newEmail.value());
        }
    }

}
