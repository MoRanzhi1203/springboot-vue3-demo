package com.example.springbootdemobackend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类 — 生成、解析、校验 Token
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(
            java.util.Base64.getEncoder().encodeToString(secret.getBytes())
        ));
    }

    /**
     * 生成 Token
     */
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 从 Token 中提取用户名（subject）
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getPayload().getSubject();
    }

    /**
     * 从 Token 中提取 userId
     */
    public Long getUserIdFromToken(String token) {
        return parseToken(token).getPayload().get("userId", Long.class);
    }

    /**
     * 判断 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 校验 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析 Token（去掉 Bearer 前缀）
     */
    private Jws<Claims> parseToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new JwtException("Token is empty");
        }
        // 去掉 Bearer 前缀
        String rawToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(rawToken);
    }
}
