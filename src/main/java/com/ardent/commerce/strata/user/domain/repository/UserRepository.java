package com.ardent.commerce.strata.user.domain.repository;

import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserId;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository Interface(port): Domain defines what it needs.
 * Infrastructure layer will implement this.

 * Independent: No JPA, Spring or Database knowledge.
 */
public interface UserRepository {

    /**
     * Persist user aggregate.
     */
    void save(User user);

    /**
     * Load user by ID.
     */
    Optional<User> findById(UserId id);

    /**
     * Load user by Keycloak ID.
     * Used for jwt to user mapping.
     */
    Optional<User> findByKeycloakId(UUID keycloakId);

    /**
     * Load user by email.
     */
    Optional<User> findByEmail(Email email);

    /**
     * Check if email already registered.
     */
    boolean existsByEmail(Email email);

    /**
     * Remove user aggregate from persistence.
     */
    void delete(User user);

}
