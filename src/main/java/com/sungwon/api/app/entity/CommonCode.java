package com.sungwon.api.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonCode {
    // 그룹코드
    private String grpCd;

    // 코드
    private String cd;

    // 코드명
    private String cdNm;

    // 비고
    private String remark;

    // 참조값1
    private String ref1Val;

    // 참조값2
    private String ref2Val;

    // 참조값3
    private String ref3Val;
}
