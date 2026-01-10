package com.ardent.commerce.strata.shared.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JWT Authentication Converter.
 * Converts Spring's JWT to AuthenticationToken with roles extracted from Keycloak.

 * Flow:
 * 1. Spring Security validates JWT signature
 * 2. JwtAuthConverter extracts authorities from JWT claims
 * 3. Creates JwtAuthenticationToken with roles (ROLE_ADMIN, ROLE_USER, etc.)
 * 4. SecurityContext uses token for authorization checks

 * JWT Structure Expected:
 * {
 *   "sub": "user-id",
 *   "preferred_username": "john@example.com",
 *   "resource_access": {
 *     "strata-backend": {
 *       "roles": ["admin", "user"]
 *     }
 *   }
 * }
 */
@RequiredArgsConstructor
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties properties;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        // Combine standard authorities with Keycloak resource roles
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        // Use configured principal attribute or default SUB
        String claimName = properties.getPrincipalAttribute() == null ?
                JwtClaimNames.SUB : properties.getPrincipalAttribute();

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(claimName));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        // Get resource_access claim
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        // Get application specific resource
        Map<String, Object> resource;
        // Get roles
        Collection<String> resourceRoles;

        // Null check for any properties or roles
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Set.of();
        }

        // Extract roles [admin, user] and convert into [ROLE_ADMIN, ROLE_USER]
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
