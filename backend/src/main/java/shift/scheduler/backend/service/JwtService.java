package shift.scheduler.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shift.scheduler.backend.model.Account;
import shift.scheduler.backend.model.jwt.CachedJwt;
import shift.scheduler.backend.repository.JwtRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Handles JSON web tokens. Based on
 * https://github.com/ali-bouali/spring-boot-3-jwt-security/blob/main/src/main/java/com/alibou/security/config/JwtService.java.
 */
@Service
public class JwtService {

    // Assuming that the Authorization header value begins with "Bearer "
    private static final int HEADER_JWT_START_INDEX = 7;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    @Value("${application.security.jwt.refresh-token}")
    private long refresh;

    @Autowired
    private JwtRepository jwtRepository;

    public String extractTokenFromHeader(String header) {
        return header.substring(HEADER_JWT_START_INDEX);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", ((Account) userDetails).getRole());

        return buildToken(claims, userDetails);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if the provided token is usable by the specified user. The token must
     * be valid and stored in a Redis cache for it to be usable.
     */
    public boolean isTokenUsable(String token, UserDetails userDetails) {

        String username = userDetails.getUsername();
        boolean isCached = jwtRepository.existsById(username);

        return isTokenValid(token, userDetails) && isCached;
    }

    /**
     * Checks if the given token is valid by verifying that it is associated with the
     * specified user, and that it has not expired yet.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = extractUsername(token);

        boolean isExpired = extractClaim(token, Claims::getExpiration).before(new Date());
        boolean isValidUser = username.equals(userDetails.getUsername());

        return !isExpired && isValidUser;
    }

    public void saveToken(String token) {

        String username = extractUsername(token);
        jwtRepository.save(new CachedJwt(username, token));
    }

    /**
     * Checks the Redis cache for a
     * @param userDetails
     * @return
     */
    public String findOrCreateToken(UserDetails userDetails) {

        String username = userDetails.getUsername();

        CachedJwt cachedJwt = jwtRepository.findById(username).orElse(null);

        if (cachedJwt == null || !isTokenValid(cachedJwt.getToken(), userDetails)) {
            String token = generateToken(userDetails);

            saveToken(token);
            return token;
        } else {
            return cachedJwt.getToken();
        }
    }

    public boolean deleteToken(String token) {

        String username = extractUsername(token);
        CachedJwt cachedJwt = jwtRepository.findById(username).orElse(null);

        if (cachedJwt == null || !token.equals(cachedJwt.getToken()))
            return false;

        jwtRepository.deleteById(username);
        return true;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails) {

        return Jwts
                .builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    // Based on https://github.com/jwtk/jjwt#reading-a-jwt
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSecretKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
