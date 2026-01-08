package com.ardent.commerce.strata.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository: Database access.
 * PRIVATE to infrastructure - never exposed to domain or application.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByKeycloakId(UUID keycloakId);
    boolean existsByEmail(String email);
}
