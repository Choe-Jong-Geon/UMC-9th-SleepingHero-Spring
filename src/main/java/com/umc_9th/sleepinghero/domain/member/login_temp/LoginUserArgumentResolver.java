package com.umc_9th.sleepinghero.domain.member.login_temp;

import com.umc_9th.sleepinghero.domain.member.entity.Member;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 파라미터에 @LoginUser 어노테이션이 붙어 있는지 확인
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // [임시 개발용] 로그인 기능이 없으므로 무조건 ID 1번인 테스트 유저 정보를 반환
        // 나중에 로그인 기능이 완성되면 여기서 세션이나 JWT에서 유저를 꺼내는 로직으로 변경합니다.
        return Member.builder()
                .email("bjy829@naver.com")
                .nickName("loui")
                .build();
    }

}
