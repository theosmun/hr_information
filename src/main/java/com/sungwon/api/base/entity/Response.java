package com.sungwon.api.base.entity;

import com.sungwon.api.base.advice.ApiError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true) // 체이닝 활성화
public class Response<T> {
    // 에러 아이디
    private String errorId = "";

    // 코드
    private String code = "0000";

    // 성공 여부
    private boolean isSuccess = true;

    // 메시지
    private String message = "";

    // 데이터
    private T payload;

    public Response(ApiError apiError) {
        this.errorId = UUID.randomUUID().toString();
        this.code = apiError.getCode();
        this.message = apiError.getMessage();
        this.isSuccess = false;
    }
}
