package com.umc_9th.sleepinghero.domain.test.controller;




import com.umc_9th.sleepinghero.domain.test.converter.TestConverter;
import com.umc_9th.sleepinghero.domain.test.dto.res.TestResDTO;
import com.umc_9th.sleepinghero.domain.test.service.TestQueryService;
import com.umc_9th.sleepinghero.global.apiPayload.ApiResponse;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralErrorCode;
import com.umc_9th.sleepinghero.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestQueryService testQueryService;

    @GetMapping
    public ApiResponse<TestResDTO.Testing> test() throws Exception {
        // 응답 코드 정의
        GeneralSuccessCode code = GeneralSuccessCode.OK;
        return ApiResponse.onSuccess(
                code,
                TestConverter.toTestingDTO("This is Test!")
        );
    }

    // 예외 상황
    @GetMapping("/exception")
    public ApiResponse<TestResDTO.Exception> exception(
            @RequestParam Long flag
    ) {

        testQueryService.checkFlag(flag);

        // 응답 코드 정의
        GeneralErrorCode code = GeneralErrorCode.BAD_REQUEST;
        return ApiResponse.onFailure(code, TestConverter.toExceptionDTO("This is Test!"));
    }
}
