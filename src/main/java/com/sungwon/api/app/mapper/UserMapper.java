package com.sungwon.api.app.mapper;

import com.sungwon.api.app.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 사용자 정보 조회 ( 아이디 )
     */
    User getUserByUserId(String userId);
}
