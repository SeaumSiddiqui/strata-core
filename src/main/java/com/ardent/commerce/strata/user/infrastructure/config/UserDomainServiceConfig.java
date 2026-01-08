package com.ardent.commerce.strata.user.infrastructure.config;

import com.ardent.commerce.strata.user.domain.repository.UserRepository;
import com.ardent.commerce.strata.user.domain.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration: Wires up domain service dependencies as they have no Spring annotations.
 */
@Configuration
public class UserDomainServiceConfig {
    @Bean
    public UserDomainService userDomainService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }
}
