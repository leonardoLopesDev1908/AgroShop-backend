package com.dailycodework.agroshop.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.security.oauth2.CustomOAuth2User;
import com.dailycodework.agroshop.security.user.ShopUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.accessExpirationInMils}")
    private String expirationTime;

    @Value("${auth.token.refreshExpirationInMils}")
    private String refreshTime;

    public String generateAccessToken(Authentication authentication){
        
        ShopUserDetails shop = (ShopUserDetails) authentication.getPrincipal();

        List<String> roles = shop.getAuthorities()
            .stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority()).toList();

        return Jwts.builder()
            .setSubject(shop.getEmail())   
            .claim("type", "access")
            .claim("id", shop.getId())
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(calculateExpirationDate(expirationTime))
            .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(String email){
        return Jwts.builder()
            .claim("type", "refresh")
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(calculateExpirationDate(refreshTime))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact(); 
    }

    public String generateAccessTokenOAuth2(CustomOAuth2User oAuth2User){
        return Jwts.builder()
                .claim("type", "access")
                .setSubject(oAuth2User.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(calculateExpirationDate(expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshTokenOAuth2(CustomOAuth2User oAuth2User){
        return Jwts.builder()
                .claim("type", "refresh")
                .setSubject(oAuth2User.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(calculateExpirationDate(refreshTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameDoToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()   
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token){
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody();
            
        return "refresh".equals(claims.get("type"));
    }

    public boolean isAccessToken(String token){
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody();
            
        return "access".equals(claims.get("type"));
    }

    private Date calculateExpirationDate(String expirationTimeString){
        long expirationTime = Long.parseLong(expirationTimeString);
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
