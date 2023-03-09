package com.project.runningcrew.security;


import com.project.runningcrew.user.entity.User;
import com.project.runningcrew.userrole.entity.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtProvider {
    private final String SECRET_KEY = "jflsdjafls";
    private final Long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 12; //12 시간
    private final Long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 14; // 14일

    public String createAccessToken(User user, UserRole userRole) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(user.getEmail())
                .claim("role", userRole.getRole().getValue())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


}
