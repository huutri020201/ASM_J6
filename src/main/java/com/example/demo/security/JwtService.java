package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String SECRET = "0123456789.0123456789.0123456789"; // >= 256 bit

    // Tạo token kèm lastPasswordResetDate

    public String create(User user, int expirySeconds) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + 1000L * expirySeconds);

        // Lấy danh sách role là String
        List<String> roleNames = user.getUserRoles() == null
                ? List.of("USER")
                : user.getUserRoles().stream()
                .map(r -> r.getRole().getId()) // hoặc getName()
                .toList();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim("roles", roleNames)  // chỉ gửi danh sách string
                .claim("fullname", user.getFullname()) // thêm fullname
                .claim("lastReset", user.getLastPasswordResetDate() != null
                        ? user.getLastPasswordResetDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        : now)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // Lấy body claims
    public Claims getBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate token dựa vào expiration và lastPasswordResetDate
    public boolean validate(Claims claims, User user) {
        boolean notExpired = claims.getExpiration().after(new Date());
        long tokenLastReset = claims.get("lastReset", Long.class);
        long userLastReset = user.getLastPasswordResetDate() != null
                ? user.getLastPasswordResetDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                : 0L;
        return notExpired && tokenLastReset >= userLastReset;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
