package edu.ntnu.idatt2105.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The configuration for the security. This class is used to configure the security filter chain. It uses the
 * JWTAuthenticationFilter and the AuthenticationProvider to configure the security filter chain.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * This method is used to configure the security filter chain.
     *
     * @param http The http security.
     * @return The security filter chain.
     * @throws Exception If the security filter chain cannot be created.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().and().authorizeHttpRequests().requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();

        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/images/**").permitAll()
                .requestMatchers("/api/listing/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/category/**").permitAll()
                .requestMatchers("/api/user/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
