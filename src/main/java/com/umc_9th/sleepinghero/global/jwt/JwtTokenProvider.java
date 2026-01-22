package com.umc_9th.sleepinghero.global.jwt;

import com.umc_9th.sleepinghero.domain.member.enums.Role;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessExpMillis;
    private final long refreshExpMillis;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-exp-min}") long accessExpMin,
            @Value("${jwt.refresh-exp-days}") long refreshExpDays
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMillis = Duration.ofMinutes(accessExpMin).toMillis();
        this.refreshExpMillis = Duration.ofDays(refreshExpDays).toMillis();
    }

    public String createAccessToken(Long memberId, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpMillis);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshExpMillis);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public Duration refreshTtl() {
        return Duration.ofMillis(refreshExpMillis);
    }

    public Long getMemberId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public String getRole(String token) {
        Object role = parseClaims(token).get("role");
        return role == null ? null : role.toString();
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }
    }
}
