package com.sungwon.api.app.dto;

import com.sungwon.api.base.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfo extends BaseEntity {
    // 사용자 번호
    private Long userNo;

    // 아이디
    private String userId;

    // 사용자명
    private String userName;

    // 비고
    private String remark;

    // 권한
    private String role;
}
