package com.ardent.commerce.strata.user.infrastructure.mapper;

import com.ardent.commerce.strata.user.application.dto.UserResponse;
import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.Phone;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserId;
import com.ardent.commerce.strata.user.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

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
        return new UserJpaEntity(
                user.getId().value(),
                user.getKeycloakId(),
                user.getEmail().value(),
                user.getPhone().value(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * JPA Entity -> Domain User (for loading/finding)
     */
    public User toDomain(UserJpaEntity entity) {
        return User.reconstruct(
                UserId.of(entity.getId()),
                entity.getKeycloakId(),
                Email.of(entity.getEmail()),
                Phone.of(entity.getPhone()),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Domain User -> Response DTO (for HTTP response)
     */
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId().value(),
                user.getEmail().value(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone().value(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
