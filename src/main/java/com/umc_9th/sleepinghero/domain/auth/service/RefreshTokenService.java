package com.umc_9th.sleepinghero.domain.auth.service;

import java.time.Duration;
import java.util.Optional;

public interface RefreshTokenService {

    void save(Long memberId, String refreshToken, Duration ttl);

    Optional<String> find(Long memberId);

    void delete(Long memberId);

    boolean matches(Long memberId, String refreshToken);


}
