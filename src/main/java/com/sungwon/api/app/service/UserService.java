package com.sungwon.api.app.service;

import com.sungwon.api.app.dto.UserInfo;
import com.sungwon.api.app.dto.UserSearch;
import com.sungwon.api.app.entity.User;
import com.sungwon.api.app.mapper.UserMapper;
import com.sungwon.api.base.advice.ApiError;
import com.sungwon.api.base.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.password}")
    private String DEFAULT_PASSWORD;

    /**
     * 사용자 목록 조회
     * @author : 문성원
     * @since  : 2025-01-05
     */
    public List<User> getUserList(UserSearch userSearch) {
        return userMapper.getUserList(userSearch);
    }

    /**
     * 사용자 목록 조회(아이디)
     * @author : 문성원
     * @since  : 2025-01-05
     */
    public User getUserByUserId(UserSearch userSearch){
        return userMapper.getUserByUserId(userSearch.getUserId());
    }

    /**
     * 로그인 사용자 조회
     * @author : 문성원
     * @since  : 2025-01-08
     */
    public UserInfo getUserData(String userId) {
        User user = userMapper.getUserByUserId(userId);
        UserInfo userInfo = UserInfo.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .remark(user.getRemark())
                .role(user.getRole())
                .build();

        userInfo.setCreateDtm(user.getCreateDtm());
        userInfo.setUpdateDtm(user.getUpdateDtm());

        return userInfo;
    }

    /**
     * 사용자 목록 저장
     *
     * @author : 문성원
     * @since  : 2025-01-08
     */
    @Transactional
    public void saveUserList(List<User> userInfoList) {
        userInfoList.forEach(info -> {
            User user = userMapper.existsByUserId(info.getUserNo(), info.getUserId());
            if (user != null) {
                throw new CustomException("이미 등록된 아이디 입니다.\n( " + info.getUserId() + " )");
            }
        });

        userMapper.saveUserList(userInfoList);
    }

    /**
     * 사용자 목록 삭제
     *
     * @author : 문성원
     * @since : 2025.01.09
     */
    public void deleteUserList(List<User> userInfoList) {
        userMapper.deleteUserList(userInfoList);
    }

    /**
     * 비밀번호 일치 여부 조회
     *
     * @author : 문성원
     * @since : 2025.01.09
     */
    public Boolean matchPassword(UserSearch search) {
        User user = userMapper.getUserByUserId(search.getUserId());
        if(user == null){
            throw new CustomException(ApiError.USER_NOT_FOUND);
        }
        return passwordEncoder.matches(search.getPassword(), user.getPassword());
    }

    /**
     * 비밀번호 초기화
     *
     * @author : 문성원
     * @since : 2025.01.09
     */
    public void resetPassword(String userId) {
        User user = userMapper.getUserByUserId(userId);
        if(user == null){
            throw new CustomException(ApiError.USER_NOT_FOUND);
        }

        String password = passwordEncoder.encode(DEFAULT_PASSWORD);
        user.setPassword(password);

        userMapper.saveUserInfo(user);
    }

    /**
     * 비밀번호 변경
     *
     * @author : 문성원
     * @since : 2025.01.09
     */
    public void changePassword(UserSearch search) {
        User user = userMapper.getUserByUserId(search.getUserId());
        if(user == null){
            throw new CustomException(ApiError.USER_NOT_FOUND);
        }

        if(!passwordEncoder.matches(search.getPrevPassword(), user.getPassword())){
            throw new CustomException(ApiError.USER_NOT_FOUND);
        }

        String password = passwordEncoder.encode(search.getNewPassword());

        user.setPassword(password);

        // 사용자 저장
        userMapper.saveUserInfo(user);
    }
}
