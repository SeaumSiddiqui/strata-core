package com.ardent.commerce.strata.user.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity: Database representation of User.
 * NOT used in domain - only for persistence.
 * Mapped back to domain User aggregate by UserMapper.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_keycloak_id", columnList = "keycloak_id")
})
public class UserJpaEntity {
    @Id
    @Column(columnDefinition = "UUID", name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "keycloak_id", nullable = false, unique = true, columnDefinition = "UUID")
    private UUID keycloakId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
