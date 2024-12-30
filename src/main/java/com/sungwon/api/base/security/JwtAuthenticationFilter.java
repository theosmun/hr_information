package com.sungwon.api.base.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungwon.api.app.entity.User;
import com.sungwon.api.app.enums.RoleCode;
import com.sungwon.api.app.mapper.UserMapper;
import com.sungwon.api.base.advice.ApiError;
import com.sungwon.api.base.entity.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private static final String BEARER = "Bearer";
    private static final String GAP = " ";
    private final UserMapper userMapper;
    private final JwtTokenUtils jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            if(request.getRequestURI().startsWith("api/suth")){
                filterChain.doFilter(request, response);
                return;
            }
            String authHeader = request.getHeader("Authorization");
            if(authHeader == null){
                String token = authHeader.startsWith(BEARER) ? authHeader.split(GAP)[1] : authHeader;

                Claims claims  = jwtUtil.getTokenInfo(token);
                String userId = claims.get("Userid").toString();

                // 사용자 정보
                User userInfo = userMapper.getUserByUserId(userId);

                // 권한 정보
                RoleCode roleCode = RoleCode.findByCode(userInfo.getRole());
                Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(roleCode.getRole()));

                // 인증
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            // 다음 필터
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            Response<String> errorResponse;
            if (e instanceof ExpiredJwtException) {
                errorResponse = new Response<>(ApiError.TOKEN_EXPIRED);
                log.error("Error ID: {}, Token Expired", errorResponse.getErrorId());

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else if (e instanceof JwtException) {
                errorResponse = new Response<>(ApiError.TOKEN_INVALID);
                log.error("Error ID: {}, Exception: {}", errorResponse.getErrorId(), e.getMessage(), e);

                response.setStatus(HttpStatus.BAD_REQUEST.value());
            } else {
                errorResponse = new Response<>(ApiError.SYSTEM_USER_ERROR);
                log.error("Error ID: {}, Exception: {}", errorResponse.getErrorId(), e.getMessage(), e);

                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }

            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
    }
}
