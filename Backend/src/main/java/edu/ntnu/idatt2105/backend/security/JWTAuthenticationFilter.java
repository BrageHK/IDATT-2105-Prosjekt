package edu.ntnu.idatt2105.backend.security;

import jakarta.servlet.ServletException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The authentication filter for the application. This class is used to authenticate the user. It uses the JWTService to
 * extract the user email from the JWT. It then uses the user details service to load the user by email. If the user is
 * found, the user is authenticated. If the user is not found, the user is not authenticated.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;

    /**
     * Filters the request. This method is used to filter the request. It extracts the JWT from the Authorization header.
     * It then extracts the user email from the JWT. It then uses the user details service to load the user by email. If
     * the user is found, the user is authenticated. If the user is not found, the user is not authenticated. If the
     * user is an admin, the user is granted the admin role in the security context.
     *
     * @param request The request.
     * @param response The response.
     * @param filterChain The filter chain.
     * @throws ServletException If the request could not be handled.
     * @throws IOException If the request could not be handled.
     */
    @Override
    protected void doFilterInternal(@NonNull jakarta.servlet.http.HttpServletRequest request,
                                    @NonNull jakarta.servlet.http.HttpServletResponse response,
                                    @NonNull jakarta.servlet.FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            logger.info("Missing or invalid Authorization header.");
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        logger.info("User " + userEmail + " authenticated with priveleges: "
                + SecurityContextHolder.getContext().getAuthentication().getAuthorities() + ".");
        filterChain.doFilter(request, response);

    }
}
