package com.sungwon.api.app.service;

import com.sungwon.api.app.dto.UserInfo;
import com.sungwon.api.app.dto.UserSearch;
import com.sungwon.api.app.entity.User;
import com.sungwon.api.app.mapper.UserMapper;
import com.sungwon.api.base.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;

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
    public void saveUserList(List<User> userInfoList) {
        userInfoList.forEach(info-> {
            User user = userMapper.getUserByUserId(info.getUserId());
            if(user != null){
                throw new CustomException("이미 등록된 아이디 입니다.\n( " + info.getUserId() + " )");
            }
        });
        userMapper.saveUserList(userInfoList);
    }
}
