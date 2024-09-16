package com.shuai.auth.controller;


import com.shuai.api.dto.user.LoginFormDTO;
import com.shuai.auth.service.IAccountService;
import com.shuai.common.constants.JwtConstants;
import com.shuai.common.exceptions.BadRequestException;
import com.shuai.common.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * 账户登录相关接口
 */
@RestController
@RequestMapping("/accounts")
@Api(tags = "账户管理")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @ApiOperation("登录并获取token")
    @PostMapping(value = "/login")
    public String loginByPw(@RequestBody LoginFormDTO loginFormDTO) throws UnsupportedEncodingException {
        return accountService.login(loginFormDTO, false);
    }

    @ApiOperation("管理端登录并获取token")
    @PostMapping(value = "/admin/login")
    public String adminLoginByPw(@RequestBody LoginFormDTO loginFormDTO) throws UnsupportedEncodingException {
        return accountService.login(loginFormDTO, true);
    }

    @ApiOperation("退出登录")
    @PostMapping(value = "/logout")
    public void logout() throws UnsupportedEncodingException {
        accountService.logout();
    }

    @ApiOperation("刷新token")
    @GetMapping(value = "/refresh")
    public String refreshToken(
            @CookieValue(value = JwtConstants.REFRESH_HEADER, required = false) String studentToken,
            @CookieValue(value = JwtConstants.ADMIN_REFRESH_HEADER, required = false) String adminToken
    ) throws UnsupportedEncodingException {
        if (studentToken == null && adminToken == null) {
            throw new BadRequestException("登录超时");
        }
        String host = WebUtils.getHeader("origin");
        if (host == null) {
            throw new BadRequestException("登录超时");
        }
        String token = host.startsWith("www", 7) ? studentToken : adminToken;
        if (token == null) {
            throw new BadRequestException("登录超时");
        }
        return accountService.refreshToken(WebUtils.cookieBuilder().decode(token));
    }
}
