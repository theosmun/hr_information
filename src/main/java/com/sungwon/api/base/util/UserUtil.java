package com.sungwon.api.base.util;

import com.sungwon.api.app.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class UserUtil {
    private UserUtil() {}

    public static User getUserDetail() {
        // SecurityContextHolder에서 현재 보안 컨텍스트를 가져와 Authentication 객체를 반환
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                // Authentication 객체가 null이 아니고, getPrincipal()이 User 타입인지 확인
                .filter(authentication -> authentication.getPrincipal() instanceof User)
                // getPrincipal()을 User 타입으로 변환하여 반환
                .map(authentication -> (User) authentication.getPrincipal())
                // Optional에 값이 없으면 새로운 User 객체를 반환
                .orElse(new User());
    }
}
