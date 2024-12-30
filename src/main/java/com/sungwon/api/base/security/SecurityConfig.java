package com.sungwon.api.base.security;

import com.sungwon.api.app.enums.RoleCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] ADMIN_URL = {
            "/api/menu",
            "/api/column",
            "/api/code",
            "/api/user"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보호를 비활성화 (API 또는 특정 사용 사례에서는 필요하지 않을 수 있음)
        http.csrf(AbstractHttpConfigurer::disable)

                // CORS(Cross-Origin Resource Sharing)를 활성화하고, 커스텀 설정 소스를 제공
                .cors(cors -> cors.configurationSource(corsFilter()))

                // HTTP 요청에 대한 인가 규칙을 설정
                .authorizeHttpRequests(authorize -> authorize
                        // ADMIN_URL에 매칭되는 URL에 대해 ADMIN 역할을 가진 사용자만 접근 허용
                        .requestMatchers(ADMIN_URL).hasRole(RoleCode.ADMIN.getCd())
                        // /api/auth/** 하위 URL은 인증 없이 모두 접근 가능 (로그인, 회원가입 등 공개 API 용도)
                        .requestMatchers("/api/auth/**").permitAll()
                        // 그 외의 모든 요청은 인증이 필요
                        .anyRequest().authenticated()
                )

                // 커스텀 JWT 인증 필터를 기본 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                // 접근 거부 상황에 대한 커스텀 예외 처리를 정의
//                .exceptionHandling(error -> error.accessDeniedHandler(customAccessDeniedHandler));

        // 설정된 SecurityFilterChain을 생성하여 반환
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager aucorsthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 설정 적용
        return source;
    }
}
