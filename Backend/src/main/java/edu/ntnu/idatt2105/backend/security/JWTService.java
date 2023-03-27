package edu.ntnu.idatt2105.backend.security;


import edu.ntnu.idatt2105.backend.repository.UserRepository;
import edu.ntnu.idatt2105.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The service for JWT. This service is used to generate and validate JWT tokens. It also contains methods to check if
 * the user is authenticated and if the user is an admin.
 *
 * @author Brage H. Kvamme
 * @version 1.0
 */
@Service
public class JWTService {

    // This key is used for decoding the JWT token. It should be kept secret.
    private static final String KEY_SECRET = "5368566D597133743677397A24432646294A404E635266556A576E5A72347537";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Generates a JWT token.  It takes a map of claims and a UserDetails. The claims are added to the token. The
     * username is added to the token. The token is signed with the secret key and the HS256 algorithm. The token
     * expires after 60 minutes.
     *
     * @param extraClaims The claims to add to the token.
     * @param userDetails The user details.
     * @return The JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token.  It takes a map of claims and a UserDetails. The claims are added to the token. The
     * username is added to the token. The token is signed with the secret key and the HS256 algorithm. The token
     * expires after 60 minutes.
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", ((User) userDetails).getId());
        return generateToken(claims, userDetails);
    }

    /**
     * Checks if the token is valid. It checks if the token is expired and if the username in the token is the same as
     * the username in the UserDetails.
     *
     * @param token The token to check.
     * @param userDetails The user details.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if a user is authenticated.
     *
     * @return True if the user is authenticated, false otherwise.
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    /**
     * Gets the ID of the authenticated user.
     *
     * @return The ID of the authenticated user. Returns null if the user is not authenticated.
     */
    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        return null;
    }

    /**
     * Gets the email of the authenticated user.
     *
     * @return The email of the authenticated user. Returns null if the user is not authenticated.
     */
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Checks if the token is expired.
     *
     * @param token The token to check.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Gets the expiration date of the token.
     *
     * @param token The token.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the username (email) from the token.
     * @param token The token.
     * @return The username (email) from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the ID of the user from the token.
     *
     * @param token The token.
     * @return The ID of the user from the token.
     */
    public long extractId(String token) {
        String mail = extractUsername(token);
        return userRepository.findByEmail(mail).get().getId();
    }

    /**
     * Extracts a claim from the token.
     *
     * @param token The token.
     * @param claimsResolver The function to extract the claim.
     * @param <T> The type of the claim.
     * @return The claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all the claims from the token. The token is signed with the secret key and the HS256 algorithm.
     *
     * @param token The token.
     * @return The claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets the secret sign in key.
     *
     * @return The secret sign in key.
     */
    private Key getSignInKey() {
        byte[] apiKeySecretBytes = Decoders.BASE64.decode(KEY_SECRET);
        return Keys.hmacShaKeyFor(apiKeySecretBytes);
    }
}
