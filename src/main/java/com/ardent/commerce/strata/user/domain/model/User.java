package com.ardent.commerce.strata.user.domain.model;

import com.ardent.commerce.strata.shared.domain.AggregateRoot;
import com.ardent.commerce.strata.user.domain.event.*;
import com.ardent.commerce.strata.user.domain.exception.DomainInvariantException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static com.ardent.commerce.strata.user.domain.event.UserRoleChangedEvent.RoleChangeType.ASSIGNED;
import static com.ardent.commerce.strata.user.domain.event.UserRoleChangedEvent.RoleChangeType.REMOVED;

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
    @ToString.Include
    private Set<UserRole> roles;

    @NonNull
    private LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;

    @ToString.Include
    private  boolean isActive;

    private LocalDateTime deletedAt;

    /**
     * Factory method: Create new user.
     * Called during user registration flow.
     */
    public static User create(UUID keycloakId, Email email, Phone phone, String firstName, String lastName, Set<UserRole> roles) {

        if (roles == null || !roles.contains(UserRole.of(UserRole.RoleType.CUSTOMER))) {
            throw new DomainInvariantException("New users must be assigned the default CUSTOMER role.");
        }

        UserId id = UserId.generate();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(id, keycloakId, email, phone, firstName, lastName, roles, now, now, true, null);
        user.addDomainEvent(new UserCreatedEvent(id.value(), email.value(), keycloakId, now));

        return user;
    }

    /**
     * Factory method: Reconstruct user from database (no events).
     * Called during repository fetch.
     */
    public static User reconstruct(UserId id, UUID keycloakId, Email email, Phone phone, String firstName, String lastName,
                                   Set<UserRole> roles, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isActive,
                                   LocalDateTime deletedAt) {
        if (roles == null || !roles.contains(UserRole.of(UserRole.RoleType.CUSTOMER))) {
            throw new IllegalStateException("Persistent state for User is invalid: missing default CUSTOMER role.");
        }

        return new User(id, keycloakId, email, phone, firstName, lastName, roles, createdAt, updatedAt, isActive, deletedAt);
    }

    /**
     * Business rule: Update user profile.
     * Only firstName, lastName, phone can be updated.
     */
    public void updateUserProfile(String firstName, String lastName, @NonNull Phone phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserProfileUpdatedEvent(id.value(), email.value(), updatedAt));
    }

    /**
     * Business rule: Change email (email must be unique - validated by repository).
     */
    public void changeEmail(@NonNull Email newEmail) {
        if (newEmail.equals(this.email)) {
            throw new IllegalArgumentException("New Email cannot be same as current");
        }

        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserEmailChangedEvent(keycloakId, newEmail.value(), updatedAt));
    }

    public void assignRole(@NonNull UserRole roleToAdd) {
        if (this.roles.contains(roleToAdd)) {
            return;
        }

        this.roles.add(roleToAdd);
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserRoleChangedEvent(keycloakId, roleToAdd.value().name(), ASSIGNED, updatedAt));
    }

    public void removeRole(@NonNull UserRole roleToRemove) {
        if (this.roles.size() <= 1) {
            throw new IllegalArgumentException("User must have at least one role assigned");
        }

        if (!this.roles.contains(roleToRemove)) {
            throw new IllegalArgumentException("Role " + roleToRemove + " not assigned to this user");
        }

        if (roleToRemove.equals(UserRole.of(UserRole.RoleType.CUSTOMER))) {
            throw new IllegalArgumentException("User base role " + roleToRemove.value().name() + " cannot be removed");
        }

        this.roles.remove(roleToRemove);
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserRoleChangedEvent(keycloakId, roleToRemove.value().name(), REMOVED, updatedAt));
    }

    public void recordPasswordChange() {
        this.updatedAt = LocalDateTime.now();

        addDomainEvent(new UserPasswordChangeEvent(keycloakId, updatedAt));
    }

    /**
     * Business rule: Soft Delete from application DB (Deactivate Account).
     * Already removed form Keycloak DB.
     */
    public void markAsDeleted() {
        if (!this.isActive) {
            throw new IllegalArgumentException("User already deleted and marked as inactive");
        }
        this.isActive = false;

        LocalDateTime now = LocalDateTime.now();
        this.deletedAt = now;
        this.updatedAt = now;

        addDomainEvent(new UserDeletedEvent(keycloakId, email.value(), now));
    }

}
