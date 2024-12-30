package com.sungwon.api.base.exception;

import com.sungwon.api.app.constant.CommonConstant;
import com.sungwon.api.base.advice.ApiError;
import lombok.Data;

import java.util.UUID;

@Data
public class CustomException extends RuntimeException {
    private final String id;
    private final String code;
    private final String message;

    public CustomException() {
        super("시스템 에러 입니다.");
        this.id = UUID.randomUUID().toString();
        this.code = CommonConstant.ERROR_CODE;
        this.message = "시스템 에러 입니다.";
    }

    public CustomException(String message) {
        super(message);
        this.id = UUID.randomUUID().toString();
        this.code = CommonConstant.ERROR_CODE;
        this.message = message;
    }

    public CustomException(String code, String message) {
        super(message);
        this.id = UUID.randomUUID().toString();
        this.code = code;
        this.message = message;
    }

    public CustomException(String id, String code, String message) {
        super(message);
        this.id = id;
        this.code = code;
        this.message = message;
    }

    public CustomException(ApiError apiError) {
        super(apiError.getMessage());
        this.id = UUID.randomUUID().toString();
        this.code = apiError.getCode();
        this.message = apiError.getMessage();
    }
}