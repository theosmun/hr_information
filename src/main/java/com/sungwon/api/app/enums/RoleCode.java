package com.sungwon.api.app.enums;

import com.sungwon.api.base.advice.ApiError;
import com.sungwon.api.base.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum RoleCode {
    ADMIN("ADMIN", "관리자", "ROLE_ADMIN"),
    USER("USER", "사용자", "ROLE_USER");

    private final String cd;
    private final String cdNm;
    private final String role;

    public static RoleCode findByCode(String code) {
        return Arrays.stream(RoleCode.values())
                .filter(roleCode -> roleCode.cd.equals(code))
                .findAny()
                .orElseThrow(() -> new CustomException(ApiError.SYSTEM_USER_ERROR));
    }

}
