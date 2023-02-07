package com.github.alexkhromov.security;

import com.github.alexkhromov.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.json.Json;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;
import static java.util.Base64.getEncoder;
import static java.util.stream.Collectors.toList;

@Component
public class JwtTokenProvider {

    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_METADATA = "metadata";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private long expiration;

    public String generateToken(Authentication authentication, User user) {

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList());

        String metadata = Json.createObjectBuilder()
                .add(EMAIL, user.getEmail())
                .add(USERNAME, user.getUsername())
                .build()
                .toString();

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(CLAIM_ROLES, roles);
        claimsMap.put(CLAIM_METADATA, getEncoder().encodeToString(metadata.getBytes()));

        Claims claims = new DefaultClaims(claimsMap);
        claims.setSubject(user.getEmail());
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(currentTimeMillis() + expiration));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(HS512, getEncoder().encodeToString(secret.getBytes()))
                .compact();
    }

    public String getUserEmail(String token) throws ExpiredJwtException {

        Claims claims = Jwts.parser()
                .setSigningKey(getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}