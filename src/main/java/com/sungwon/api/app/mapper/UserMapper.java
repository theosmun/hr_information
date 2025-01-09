package com.sungwon.api.app.mapper;

import com.sungwon.api.app.dto.UserSearch;
import com.sungwon.api.app.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 사용자 정보 조회 ( 아이디 )
     */
    User getUserByUserId(String userId);

    /**
     * 사용자 저장 ( 생성 저장 )
     */
    void saveUserInfo(User userInfo);

    /**
     * 사용자 목록 조회
     */
    List<User> getUserList(UserSearch userSearch);

    /**
     * 사용자 아이디 존재 확인
     */
    User existsByUserId(Long userNo, String userId);

    /**
     * 사용자 목록 저장
     */
    void saveUserList(List<User> userInfoList);

    /**
     * 사용자 목록 삭제
     */
    void deleteUserList(List<User> userInfoList);
}
