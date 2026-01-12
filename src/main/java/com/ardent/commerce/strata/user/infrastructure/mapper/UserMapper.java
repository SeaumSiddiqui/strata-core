package com.ardent.commerce.strata.user.infrastructure.mapper;

import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.*;
import com.ardent.commerce.strata.user.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper: Converts between domain User,
 * JPA UserJpaEntity, and DTO UserResponse.
 */
@Component
public class UserMapper {

    /**
     * Domain User -> JPA Entity (for saving)
     */
    public UserJpaEntity toJpaEntity(User user) {
        // Convert Set<UserRole> from domain to Set<RoleType> Enums
        Set< UserRole.RoleType> jpaRoles = user
                .getRoles()
                .stream()
                .map(UserRole::value)
                .collect(Collectors.toSet());

        return new UserJpaEntity(
                user.getId().value(),
                user.getKeycloakId(),
                user.getEmail().value(),
                user.getPhone().value(),
                user.getFirstName(),
                user.getLastName(),
                jpaRoles,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActive(),
                user.getDeletedAt()
        );
    }

    /**
     * JPA Entity -> Domain User (for loading/finding)
     */
    public User toDomain(UserJpaEntity entity) {
        // Convert Set<RoleType> Enums to Set<UserRole> for domain
        Set<UserRole> domainRoles = entity
                .getRoles()
                .stream()
                .map(UserRole::of)
                .collect(Collectors.toSet());

        return User.reconstruct(
                UserId.of(entity.getId()),
                entity.getKeycloakId(),
                Email.of(entity.getEmail()),
                Phone.of(entity.getPhone()),
                entity.getFirstName(),
                entity.getLastName(),
                domainRoles,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive(),
                entity.getDeletedAt()
        );
    }

    /**
     * Domain User -> Response DTO (for HTTP response)
     */
    public UserResponse toResponse(User user) {
        // Convert Set<UserRole> into plain String.
        Set<String> rolesForResponse = user
                .getRoles()
                .stream()
                .map(userRole -> userRole.value().name())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId().value(),
                user.getEmail().value(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone().value(),
                rolesForResponse,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActive(),
                user.getDeletedAt()
        );
    }
}
