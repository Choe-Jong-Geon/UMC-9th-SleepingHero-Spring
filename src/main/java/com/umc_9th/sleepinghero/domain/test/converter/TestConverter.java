package com.umc_9th.sleepinghero.domain.test.converter;


import com.umc_9th.sleepinghero.domain.test.dto.res.TestResDTO;

public class TestConverter {

    // 객체 -> DTO
    public static TestResDTO.Testing toTestingDTO(
            String testing
    ) {
        return TestResDTO.Testing.builder()
                .testing(testing)
                .build();
    }
    // 객체 -> DTO
    public static TestResDTO.Exception toExceptionDTO(
            String testing
    ){
        return TestResDTO.Exception.builder()
                .testString(testing)
                .build();
    }
}