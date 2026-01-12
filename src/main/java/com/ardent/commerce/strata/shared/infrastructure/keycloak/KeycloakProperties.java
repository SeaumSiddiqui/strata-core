package com.ardent.commerce.strata.shared.infrastructure.keycloak;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app.keycloak")
public class KeycloakProperties {

    @NotBlank(message = "Keycloak Server URL is required")
    private final String serverUrl;

    @NotBlank(message = "Keycloak Realm is required")
    private final String realm;

    @NotBlank(message = "Keycloak Client ID is required")
    private final String clientId;

    @NotBlank(message = "Keycloak Client Secret is required")
    private final String clientSecret;
}
