package com.sungwon.api.app.dto;


import com.sungwon.api.base.entity.BaseEntity;
import lombok.Data;

@Data
public class UserSearch extends BaseEntity {
    // 사용자 번호
    private Long userNo;

    // 아이디
    private String userId;

    // 이름
    private String userName;

    // 비밀번호
    private String password;

    // 권한
    private String role;

    // 이전 비밀번호
    private String prevPassword;

    // 새로운 비밀번호
    private String newPassword;
}
