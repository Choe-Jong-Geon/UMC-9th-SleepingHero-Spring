package com.umc_9th.sleepinghero.domain.group.exception;

import com.umc_9th.sleepinghero.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GroupErrorCode implements BaseErrorCode {

    GROUP_NOT_MADE(HttpStatus.BAD_REQUEST, "COMMON400_1", "그룹을 만들 수 없습니다. 그룹 이름, 설명을 입력해주세요"),
    GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMON400_1", "그룹을 찾을 수 없습니다."),

    NOT_GROUP_MASTER(HttpStatus.FORBIDDEN, "COMMON400_1", "그룹장만 가능한 권한입니다."),
    ALREADY_GROUP_MEMBER(HttpStatus.BAD_REQUEST, "COMMON400_2", "이미 그룹에 속해있거나 신청 대기 중인 사용자입니다."),
    GROUP_FULL(HttpStatus.BAD_REQUEST, "COMMON400_3", "그룹 인원이 가득 찼습니다."),
    GROUP_NOT_DELETED(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON500_1","그룹을 삭제할 수 없습니다, 그룹장이고 모든 회원이 " +
            "탈퇴 되어야 합니다."),
    MASTER_NOT_EXITED(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON500_1","그룹장은 탈퇴할 수 없습니다. 마스터를 위임하세요."),
    GROUP_INSIDE_RANKING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500_1", "그룹 내 랭킹을 불러올 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
