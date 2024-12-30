package com.sungwon.api.base.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiError {
    // 시스템
    SYSTEM_SERVER_ERROR("9000", "알 수 없는 에러 입니다."),
    SYSTEM_USER_ERROR("9001", "요청 정보가 잘 못 되었습니다."),

    // 사용자
    USER_NOT_FOUND("7000", "사용자 정보를 찾을 수 없습니다."),
    USER_READY("7001", "이미 등록된 회원 입니다."),
    USER_NOT_MATCH("7002", "사용자 정보가 일치하지 않습니다."),
    USER_READY_USER_ID("7003", "이미 사용중인 아이디 입니다."),

    // Token
    TOKEN_INVALID("4000", "토큰의 정보가 올바르지 않습니다."),
    TOKEN_EXPIRED("4001", "토큰이 만료 되었습니다."),
    NOT_PERMISSION("4002", "접근 권한이 없습니다.");

    private final String code;
    private final String message;
}
