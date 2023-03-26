package edu.ntnu.idatt2105.backend.security;

import edu.ntnu.idatt2105.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The configuration for the application. This class is used to configure the authentication provider and the password
 * encoder. It also configures the user details service.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Configures the user details service. This method is used to configure the user details service. It uses the
     * userRepository to find the user by email. If the user is not found, a UsernameNotFoundException is thrown.
     *
     * @return The user details service.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Configures the authentication provider. This method is used to configure the authentication provider. It uses the
     * user details service and the password encoder to authenticate the user.
     *
     * @return The authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Configures the authentication manager. This method is used to configure the authentication manager. It uses the
     * authentication configuration to get the authentication manager. This is done to avoid a circular dependency.
     *
     * @param config The authentication configuration.
     * @return The authentication manager.
     * @throws Exception If the authentication manager cannot be created.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the password encoder. This method is used to configure the password encoder. It uses the BCrypt
     * password encoder.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
