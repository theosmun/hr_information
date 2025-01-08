package com.sungwon.api.app.controller;

import com.sungwon.api.app.constant.CommonConstant;
import com.sungwon.api.app.dto.UserInfo;
import com.sungwon.api.app.dto.UserSearch;
import com.sungwon.api.app.entity.User;
import com.sungwon.api.app.service.UserService;
import com.sungwon.api.base.entity.Response;
import com.sungwon.api.base.util.UserUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    /**
     * 사용자 목록 조회
     *
     * @author : 문성원
     * @since : 2024.12.15
     */
    @GetMapping("")
    public Response<List<User>> getUserList(UserSearch search) {
        List<User> userList = userService.getUserList(search);
        return new Response<List<User>>().setPayload(userList);
    }


    @GetMapping("{userId}")
    public Response<User> getUser(UserSearch search) {
        User user = userService.getUserByUserId(search);
        return new Response<User>().setPayload(user);
    }


    /**
     * 해당 사용자 로그인 사용 유무
     *
     * @author : 문성원
     * @since : 2024.01.08
     * EX) 해당 사용자 로그인 정보
     */
    @GetMapping("login/info")
    public Response<UserInfo> getUserData() {
        String userId = UserUtil.getUserDetail().getUserId();

        UserInfo userInfo = userService.getUserData(userId);
        return new Response<UserInfo>().setPayload(userInfo);
    }

    /**
     * 사용자 목록 저장
     *
     * @author : 정승현
     * @since : 2024.12.19
     */
    @PostMapping("")
    public Response<String> saveUserList(@RequestBody List<User> userList) {
        userService.saveUserList(userList);
        return new Response<String>().setPayload(CommonConstant.SUCCESS);
    }


    /**
     * 사용자 목록 삭제
     *
     * @author : 정승현
     * @since : 2024.12.24
     */


    /**
     * 비밀번호 일치 여부 조회
     *
     * @author : 정승현
     * @since : 2024.12.15
     */


    /**
     * 비밀번호 초기화
     *
     * @author : 정승현
     * @since : 2024.12.15
     */


    /**
     * 비밀번호 변경
     *
     * @author : 정승현
     * @since : 2024.12.15
     */


    /**
     * 비밀번호 강제 변경 ( 관리자용 )
     *
     * @author : 정승현
     * @since : 2024.12.15
     */

}
