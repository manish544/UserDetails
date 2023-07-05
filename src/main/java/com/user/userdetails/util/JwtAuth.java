package com.user.userdetails.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.user.userdetails.bean.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuth {
    private String secret = "your_secret_key";
    private long expirationTime = 86400000; // 24 hours

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token, User user) {
        String username = extractUsername(token);
        return (username.equals(user.getUserName()) && !isTokenExpired(token));
    }

    private String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiryDate = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        return expiryDate.before(new Date());
    }
}

