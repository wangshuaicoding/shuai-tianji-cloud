package com.shuai.auth.service;


import com.shuai.api.dto.user.LoginFormDTO;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 账号表，平台内所有用户的账号、密码信息 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
public interface IAccountService{

    String login(LoginFormDTO loginFormDTO, boolean isStaff) throws UnsupportedEncodingException;

    void logout() throws UnsupportedEncodingException;

    String refreshToken(String refreshToken) throws UnsupportedEncodingException;
}
