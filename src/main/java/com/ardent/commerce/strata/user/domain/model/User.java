package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.AggregateRoot;
import com.ardent.commerce.strata.user.domain.event.UserCreatedEvent;
import com.ardent.commerce.strata.user.domain.event.UserProfileUpdatedEvent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User Aggregate Root.
 * Encapsulates user data and business rules.
 * All user modifications go through this aggregate.
 */
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends AggregateRoot {
    @NonNull
    @EqualsAndHashCode.Include
    @ToString.Include
    private final UserId id;

    @NonNull
    private final UUID keycloakId;

    @NonNull
    @ToString.Include
    private Email email;

    @NonNull
    private Phone phone;

    @ToString.Include
    private String firstName;

    @ToString.Include
    private String lastName;

    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;

    /**
     * Factory method: Create new user.
     * Called during user registration flow.
     */
    public static User create(UUID keycloakId, Email email, Phone phone, String firstName, String lastName) {
        UserId id = UserId.generate();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(id, keycloakId, email, phone, firstName, lastName, now, now);
        user.addDomainEvent(new UserCreatedEvent(id.value(), email.value(), keycloakId));

        return user;
    }

    /**
     * Factory method: Reconstruct user from database (no events).
     * Called during repository fetch.
     */
    public static User reconstruct(UserId id, UUID keycloakId, Email email, Phone phone, String firstName,
                                   String lastName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(id, keycloakId, email, phone, firstName, lastName, createdAt, updatedAt);
    }

    /**
     * Business rule: Update user profile.
     * Only firstName, lastName, phone can be changed.
     */
    public void updateUserProfile(String firstName, String lastName, @NonNull Phone phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserProfileUpdatedEvent(id.value(), email.value()));
    }

    /**
     * Business rule: Change email (email must be unique - validated by repository).
     */
    public void changeEmail(@NonNull Email newEmail) {
        if (newEmail.equals(this.email)) {
            throw new IllegalArgumentException("Updated Email can't be the same as current email");
        }

        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserProfileUpdatedEvent(id.value(), newEmail.value()));
    }
}
