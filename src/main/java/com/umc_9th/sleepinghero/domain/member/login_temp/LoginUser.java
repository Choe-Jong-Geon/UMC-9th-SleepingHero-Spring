package com.umc_9th.sleepinghero.domain.member.login_temp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 파라미터에 사용하겠다고 선언
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
public @interface LoginUser {
}
