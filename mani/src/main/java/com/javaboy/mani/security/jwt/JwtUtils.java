package com.javaboy.mani.security.jwt;

import com.javaboy.mani.security.user.ShopUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.accessExpirationInMils}")
    private String expirationTime;

    @Value("${auth.token.refreshExpirationInMils}")
    private String refreshExpirationTime;

    public String generateAccessTokenForUser(Authentication authentication){
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .setIssuedAt(new java.util.Date())
                .setExpiration(calculateExpirationDate(expirationTime))
                .signWith(key(), SignatureAlgorithm.ES256).compact();
    }
    
    public String generateRefreshToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new java.util.Date())
                .setExpiration(calculateExpirationDate(refreshExpirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private Date calculateExpirationDate(String expirationTimeString){
        long expirationTime = Long.parseLong(expirationTimeString);
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
