package com.sungwon.api.base.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungwon.api.base.advice.ApiError;
import com.sungwon.api.base.entity.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String errorId = UUID.randomUUID().toString();
        log.error("Error ID: {}, Exception: {}", errorId, accessDeniedException.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        String res = objectMapper.writeValueAsString(new Response<String>(ApiError.NOT_PERMISSION));

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(res);
    }
}
