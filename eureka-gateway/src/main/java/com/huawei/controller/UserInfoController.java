package com.huawei.controller;

import com.huawei.pojo.LoginUser;
import com.huawei.pojo.Result;
import com.huawei.pojo.UserInfo;
import com.huawei.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hjf
 * @date 2022-10-19 10:26
 * @describe 用户controller
 */
@Api(tags = "用户管理")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @ApiOperation(value = "账密登录", notes = "账密登录")
    @GetMapping("/login")
    public Result<UserInfo> login(
            @ApiParam("用户名") @RequestParam("userName") String userName,
            @ApiParam("密码") @RequestParam("password") String password
    ) {
        return userInfoService.login(userName,password);
    }

    @ApiOperation(value = "根据ID获取用户", notes = "根据ID获取用户")
    @GetMapping("/getUserInfoById")
    public Result<UserInfo> getUserInfoById(@ApiParam("用户ID") @RequestParam("id") Long id) {
        return userInfoService.getUserInfoById(id);
    }

    @ApiOperation(value = "获取登录信息", notes = "获取登录信息")
    @GetMapping("/me")
    public Result<LoginUser> postAccessToken(Authentication authentication){
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return Result.ok(loginUser);
    }
}
