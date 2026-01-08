package com.ardent.commerce.strata.user.infrastructure.persistence;

import com.ardent.commerce.strata.user.domain.model.Email;
import com.ardent.commerce.strata.user.domain.model.User;
import com.ardent.commerce.strata.user.domain.model.UserId;
import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository Adapter: Implements domain repository interface.
 * Translates between domain User aggregate and JPA UserJpaEntity.
 * This is the ONLY place that knows about JPA.
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    @Override
    public void save(User user) {
        UserJpaEntity entity = userMapper.toJpaEntity(user);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value())
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByKeycloakId(UUID keycloakId) {
        return jpaRepository.findByKeycloakId(keycloakId)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value())
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }

    @Override
    public void delete(User user) {
        jpaRepository.deleteById(user.getId().value());
    }
}
