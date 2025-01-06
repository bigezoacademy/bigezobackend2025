package bigezo.code.backend;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;  // It's better to store a secure key externally in production

    @Value("${jwt.expiration}")
    private long expiration;

    // Create a secure 512-bit signing key
    private Key getSigningKey() {
        return secret != null && !secret.isEmpty() ?
                new javax.crypto.spec.SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName()) :
                Keys.secretKeyFor(SignatureAlgorithm.HS512);  // Default secure key generation if secret is missing
    }

    // Generate JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // expiration time in milliseconds
                .signWith(getSigningKey())  // Use the new secure key
                .compact();
    }

    // Validate JWT Token
    public boolean validateToken(String token, String username) {
        String jwtUsername = getUsernameFromToken(token);
        return (jwtUsername.equals(username) && !isTokenExpired(token));
    }

    // Get Username from Token
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaimsFromToken(token);
        return claims.getSubject();
    }

    // Check if Token Expired
    private boolean isTokenExpired(String token) {
        Claims claims = parseClaimsFromToken(token);
        Date expirationDate = claims.getExpiration();
        return expirationDate.before(new Date());
    }

    // Helper method to parse Claims from Token (updated for newer jjwt version)
    private Claims parseClaimsFromToken(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Secure key for parsing
                .build();
        return parser.parseClaimsJws(token).getBody();
    }
}
