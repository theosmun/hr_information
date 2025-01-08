package com.sungwon.api.app.controller;

import com.sungwon.api.app.constant.CommonConstant;
import com.sungwon.api.app.dto.LoginInfo;
import com.sungwon.api.app.dto.LoginSearch;
import com.sungwon.api.app.entity.User;
import com.sungwon.api.app.service.AuthService;
import com.sungwon.api.base.entity.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private static final String BEARER = "Bearer";
    private static final String GAP = " ";
    private final AuthService authService;

    /**
     * 회원가입
     * @author : 문성원
     * @since  : 2025.01.04
     * @param userinfo : 회원가입 요청 정보 (JSON 형식)
     * @return Response<String> : 성공 메시지 반환
     */
    @PostMapping("/register")
    public Response<String> register(@RequestBody User userinfo) {
        authService.register(userinfo);
        return new Response<String>().setPayload(CommonConstant.SUCCESS);
    }

    /**
     * 로그인
     * @author : 문성원
     * @since  : 2025.01.04
     * @param loginSearch : 로그인 정보
     * @return Response<LoginInfo> : accessToken 반환
     */
    @PostMapping("/login")
    public Response<LoginInfo> login(@RequestBody LoginSearch loginSearch) {
        LoginInfo loginInfo = authService.login(loginSearch);
        return new Response<LoginInfo>().setPayload(loginInfo);
    }

    /**
     * 로그아웃
     * @author : 문성원
     * @since  : 2025.01.04
     * @param authHeader : 로그인 정보
     * @return Response<String> : accessToken 반환
     */
    @GetMapping("/logout")
    public Response<String> logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.startsWith(BEARER) ? authHeader.split(GAP)[1] : authHeader;
        authService.logout(accessToken);
        return new Response<String>().setPayload(CommonConstant.SUCCESS);
    }

    /**
     * 관리자 여부
     * @author : 문성원
     * @since  : 2025.01.05
     * @param authHeader : 로그인 정보
     * @return Response<Boolean> : Admin 유무
     */
    @PostMapping("/admin")
    public Response<Boolean> isAdmin(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.replace(BEARER, "");
        Boolean isAdmin = authService.isAdmin(accessToken);
        return new Response<Boolean>().setPayload(isAdmin);
    }

    /**
     * AccessToken
     * @author : 문성원
     * @since  : 2025.01.05
     * @param authHeader : 로그인 정보
     * @return Response<LoginInfo> : AccessToken 반환
     */
    @GetMapping("/access")
    public Response<LoginInfo> access(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.startsWith(BEARER) ? authHeader.split(GAP)[1] : authHeader;
        LoginInfo loginInfo =authService.access(accessToken);
        return new Response<LoginInfo>().setPayload(loginInfo);
    }

    /**
     * RefreshToken
     * @author : 문성원
     * @since  : 2025.01.05
     * @param authHeader : 로그인 정보
     * @return Response<LoginInfo> : RefreshToken 반환
     */
    @GetMapping("/refresh")
    public Response<LoginInfo> refresh(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.startsWith(BEARER) ? authHeader.split(GAP)[1] : authHeader;
        LoginInfo loginInfo = authService.refresh(accessToken);
        return new Response<LoginInfo>().setPayload(loginInfo);
    }

}
