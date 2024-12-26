package com.inventorymanagementsystem.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Blacklist to store invalidated tokens
    private static final Map<String, Boolean> invalidatedTokens = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        logger.info("JWT secret: {}", secret);
        logger.info("JWT expiration: {}", expiration);
    }

    public String generateToken(String username, String email, String role, String firstName, String address) {
        return Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .claim("role", role)
                .claim("first_name", firstName)
                .claim("address", address)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return !isTokenInvalidated(token) && isValidTokenForUser(token, username);
    }

    private boolean isValidTokenForUser(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    public void invalidateToken(String token) {
        invalidatedTokens.put(token, true);
        logger.info("Token invalidated: {}", token);
    }

    private boolean isTokenInvalidated(String token) {
        return invalidatedTokens.getOrDefault(token, false);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractFirstName(String token) {
        return extractClaim(token, claims -> claims.get("first_name", String.class));
    }

    public String extractAddress(String token) {
        return extractClaim(token, claims -> claims.get("address", String.class));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired: {}", token);
            throw new RuntimeException("Token has expired", e);
        }
    }

    // New method to refresh the JWT token
    public String refreshToken(String oldJwt) {
        // Extract claims from the old token
        Claims claims = extractAllClaims(oldJwt);

        // Check if the token has expired
        if (claims.getExpiration().before(new Date())) {
            throw new RuntimeException("Token has expired and cannot be refreshed");
        }

        // Create a new token with the same claims but with a fresh expiration
        String username = claims.getSubject();
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        String firstName = claims.get("first_name", String.class);
        String address = claims.get("address", String.class);

        return generateToken(username, email, role, firstName, address);
    }
}
