package com.ivan4usa.fp.security;

import com.ivan4usa.fp.entity.User;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    private final Logger logger = LogManager.getLogger(this.getClass());

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() * SecurityConstants.SHORT_EXPIRATION_TIME);
        String userId = Long.toString(user.getId());
        Map<String, Object> claimMap = new HashMap<>();
        claimMap.put("id", userId);
        claimMap.put("email", user.getEmail());
        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claimMap)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            logger.error("Failed to validate token." + e);
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
         String id = (String) claims.get("id");
         return Long.parseLong(id);
    }
}
