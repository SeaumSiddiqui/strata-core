package com.ardent.commerce.strata.user.domain.repository;

import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserId;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository Interface(port): Infrastructure layer will implement this.
 * Independent: No JPA, Spring or Database knowledge.
 */
public interface UserRepository {

    void save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByKeycloakId(UUID keycloakId);

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    void delete(User user);

}
