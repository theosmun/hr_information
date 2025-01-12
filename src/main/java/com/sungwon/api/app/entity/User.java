package com.sungwon.api.app.entity;


import com.sungwon.api.base.entity.BaseEntity;
import lombok.Data;

@Data
public class User extends BaseEntity {
    // 사용자 번호
    private Long userNo;

    // 아이디
    private String userId;

    // 사용자명
    private String userName;

    // 비밀번호
    private String password;

    // 리프레쉬 토큰
    private String refreshToken;

    // 비고
    private String remark;

    // 권한
    private String role;

    //고유번호
    private String deviceId;
}
