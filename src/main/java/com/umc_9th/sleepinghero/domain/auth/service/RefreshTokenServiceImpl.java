package com.umc_9th.sleepinghero.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final StringRedisTemplate redis;

    private static String key(Long memberId) {
        return "refresh:" + memberId;
    }

    public void save(Long memberId, String refreshToken, Duration ttl) {
        redis.opsForValue().set(key(memberId), refreshToken, ttl);
    }

    public Optional<String> find(Long memberId) {
        return Optional.ofNullable(redis.opsForValue().get(key(memberId)));
    }

    public void delete(Long memberId) {
        redis.delete(key(memberId));
    }

    public boolean matches(Long memberId, String refreshToken) {
        return find(memberId).map(rt -> rt.equals(refreshToken)).orElse(false);
    }
}
