package com.umc_9th.sleepinghero.domain.test.exception;


import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.exception.GeneralException;

public class TestException extends GeneralException {
    public TestException(BaseErrorCode code) {
        super(code);
    }
}

