package com.sungwon.api.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    // Access Token
    private String accessToken;

    // Refresh Token
    private String refreshToken;
}
