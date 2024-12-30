package com.sungwon.api.base.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
    // 페이지
    private Integer page = 1;

    // 페이지 사이즈
    private Integer size = 10;

    // 전체 개수
    private Integer totalCount = 0;

    // 등록일
    private LocalDateTime createDtm;

    // 수정일
    private LocalDateTime updateDtm;

    // Offset
    public int getOffset() {
        return ( page - 1) * size;
    }
}