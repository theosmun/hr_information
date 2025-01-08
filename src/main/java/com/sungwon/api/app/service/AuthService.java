package com.sungwon.api.app.service;



import com.sungwon.api.app.dto.LoginInfo;
import com.sungwon.api.app.dto.LoginSearch;
import com.sungwon.api.app.entity.User;
import com.sungwon.api.app.mapper.UserMapper;
import com.sungwon.api.base.advice.ApiError;
import com.sungwon.api.base.exception.CustomException;
import com.sungwon.api.base.security.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/**
 * 인증 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtUtil;
    private static final String ADMIN = "ADMIN";

    /**
     * 회원가입
     * @author : 문성원
     * @since  : 2025.01.04
     */
    @Transactional
    public void register(User userInfo) {
        // 사용자 유무 검증
        User user = userMapper.getUserByUserId(userInfo.getUserId());
        if (user != null) {
            throw new CustomException(ApiError.USER_READY);
        }

        // 비밀번호 암호화
        String password = passwordEncoder.encode(userInfo.getPassword());
        userInfo.setPassword(password);

        // 사용자 저장
        userMapper.saveUserInfo(userInfo);
    }

    /**
     * 로그인
     * @author : 문성원
     * @since  : 2025.01.04
     */
    public LoginInfo login(LoginSearch search) {

        User user = userMapper.getUserByUserId(search.getUserId());
        // 사용자 검증
        if (user == null) {
            throw new CustomException(ApiError.USER_NOT_FOUND);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(search.getPassword(), user.getPassword())) {
            throw new CustomException(ApiError.USER_NOT_FOUND);
        }

        // 토큰 발급
        String uuid = UUID.randomUUID().toString();
        String accessToken = jwtUtil.createToken(search.getUserId(),uuid,true);
        String refreshToken = jwtUtil.createToken(search.getUserId(),uuid,false );

        // 토큰 저장
        user.setRefreshToken(refreshToken);
        userMapper.saveUserInfo(user);

        // 토큰 정보 반환
        return LoginInfo.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 로그아웃
     * @author : 문성원
     * @since  : 2025.01.04
     */
    public void logout(String accessToken) {
        Claims claims = jwtUtil.getTokenInfo(accessToken);
        String userId = claims.get("USER_ID").toString();

        User user = userMapper.getUserByUserId(userId);
        user.setRefreshToken("");

        userMapper.saveUserInfo(user);
    }

    /**
     * 관리자 여부
     * @author : 문성원
     * @since  : 2025.01.04
     */
    public Boolean isAdmin(String accessToken) {
        Claims claims = jwtUtil.getTokenInfo(accessToken);
        String userId = claims.get("USER_ID").toString();

        User user = userMapper.getUserByUserId(userId);
        return  user.getRole().equals(ADMIN);
    }

    /**
     * AccessToken
     * @author : 문성원
     * @since  : 2025.01.04
     */
    public LoginInfo access(String accessToken) {
        try{
            jwtUtil.getTokenInfo(accessToken);
            return LoginInfo.builder().accessToken(accessToken).build();
        }catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                throw new CustomException(ApiError.TOKEN_EXPIRED);
            } else {
                throw new CustomException(ApiError.TOKEN_INVALID);
            }
        }
    }

    public LoginInfo refresh(String accessToken) {
        try{
            jwtUtil.getTokenInfo(accessToken);
            return LoginInfo.builder().accessToken(accessToken).build();
        }
        catch (Exception e) {
            if(e instanceof ExpiredJwtException) {
                // Access Token 정보
                Claims claims = jwtUtil.getTokenInfo(accessToken);
                String userId = claims.get("USER_ID").toString();

                // 사용자 조회
                User user = userMapper.getUserByUserId(userId);
                Claims userClaims = jwtUtil.getTokenInfo(user.getRefreshToken());

                // 토큰 발급
                String uuid = UUID.randomUUID().toString();
                String userid = userClaims.get("USER_ID").toString();
                String newAccessToken =jwtUtil.createToken(uuid,userid,true);
                String refreshToken = jwtUtil.createToken(uuid,uuid,false );

                // 토큰 저장
                user.setRefreshToken(refreshToken);
                userMapper.saveUserInfo(user);

                return LoginInfo.builder().
                        accessToken(newAccessToken).
                        build();
            }
            else{
                throw new CustomException(ApiError.TOKEN_INVALID);
            }
        }
    }
}