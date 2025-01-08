package com.sungwon.api.base.advice;

import com.sungwon.api.base.entity.Response;
import com.sungwon.api.base.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<String>> handleCustomException(CustomException exception) {
        Response<String> response = new Response<String>()
                .setErrorId(exception.getId())
                .setCode(exception.getCode())
                .setMessage(exception.getMessage())
                .setSuccess(false);

        HttpStatus status = exception.getCode().equals("9999") ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }
}
