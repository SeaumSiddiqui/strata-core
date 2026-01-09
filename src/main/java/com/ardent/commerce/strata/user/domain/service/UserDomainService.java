package com.ardent.commerce.strata.user.domain.service;

import com.ardent.commerce.strata.user.domain.exception.DuplicateEmailException;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Domain Service: Stateless business logic.
 * Handles operations that require knowledge of entire user collection.
 */

@RequiredArgsConstructor
public class UserDomainService {
    private final UserRepository userRepository;

    /**
     * Business rule: Change user email
     */
    public void changeEmail(User user, Email newEmail) {

        if (userRepository.existsByEmail(newEmail)) {
            throw new DuplicateEmailException(newEmail.value());
        }
        user.changeEmail(newEmail);
        userRepository.save(user);
    }

}
