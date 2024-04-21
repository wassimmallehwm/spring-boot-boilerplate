package com.boilerplate.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boilerplate.configuration.ExternalConfigs;

import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    @Autowired
    private ExternalConfigs externalConfigs;

    public JwtResponse generateJwtToken(String username, Boolean isRefresh) {

        long expiration = isRefresh ?
                externalConfigs.getJwtRefreshTokenExpiration() :
                externalConfigs.getJwtAccessTokenExpiration();
        String token = Jwts.builder()
                .setSubject((username))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration * 60 * 1000L))
                .signWith(SignatureAlgorithm.HS512, externalConfigs.getJwtSecret())
                .compact();
        return new JwtResponse(token, (int) (expiration * 60));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(externalConfigs.getJwtSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(externalConfigs.getJwtSecret())
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}