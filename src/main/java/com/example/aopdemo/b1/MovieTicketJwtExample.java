package com.example.aopdemo.b1;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MovieTicketJwtExample {

    public static void main(String[] args) {
        // (1) Tạo ra Secret Key
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // (2) Định nghĩa claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123L);
        claims.put("roles", "USER");

        // (3) Thiết lập thời gian phát hành và hết hạn
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600 * 1000); // 1 giờ

        // (4) Xây dựng và ký JWT
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setSubject("user@movieticket.com")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated JWT: " + jwtToken);

        // --- Lỗi ở đây: Sử dụng một key MỚI để giải mã --- (Lỗi logic cố ý)
//        Key differentKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        try {
            var claimsBody = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            System.out.println("JWT is valid and verified!");

            String subject = claimsBody.getSubject();
            String roles = (String) claimsBody.get("roles");
            Long userId = ((Number) claimsBody.get("userId")).longValue();

            System.out.println("Subject: " + subject);
            System.out.println("User ID: " + userId);
            System.out.println("Roles: " + roles);

        } catch (Exception e) {
            System.err.println("Invalid JWT: " + e.getMessage());
        }
    }
}
