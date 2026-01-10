package com.umc_9th.sleepinghero.domain.test.service;

import com.umc_9th.sleepinghero.domain.test.exception.TestErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestQueryServiceImpl implements TestQueryService {

    @Override
    public void checkFlag(Long flag){
        if (flag == 1){
            throw new GeneralException(TestErrorCode.GENERAL_EXCEPTION);
        }
    }
}