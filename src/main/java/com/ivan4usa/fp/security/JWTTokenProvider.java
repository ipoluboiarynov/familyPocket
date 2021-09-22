package com.ivan4usa.fp.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ivan4usa.fp.entity.User;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
public class JWTTokenProvider {
    private final Logger logger = LogManager.getLogger(this.getClass());

    public static final String CLAIM_USER_KEY = "user";

    @Value("${jwt.secret_key}")
    private String secret_key;

    @Value("${jwt.expiration_time_short}")
    private int expiration_time_short;

    @Value("${jwt.expiration_time_long}")
    private int expiration_time_long;

    public String generateToken(User user) throws JsonProcessingException {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expiration_time_short);
        Map<String, Object> claimMap = new HashMap<>();

//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(user);
//
//        String base64 = Base64.encodeBase64String(json.getBytes(StandardCharsets.UTF_8));



        claimMap.put(CLAIM_USER_KEY, user);
        claimMap.put(Claims.SUBJECT, user.getId());

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret_key);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setClaims(claimMap)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    /**
     * Validate input token
     * @param token jwt token
     * @return true or false about token validation
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret_key))
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: " + token);
        } catch (SignatureException e) {
            logger.error("JWT token is not signed: " + token);
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: " + token);
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: " + token);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: " + token);
            System.out.println(e);
        }

        return false;
    }

    public User getUserFromToken(String token) throws IllegalArgumentException {
        String json = Jwts.parser()
                .setSigningKey(secret_key)
                .parseClaimsJws(token)
                .getBody()
                .get(CLAIM_USER_KEY).toString();
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }
}
