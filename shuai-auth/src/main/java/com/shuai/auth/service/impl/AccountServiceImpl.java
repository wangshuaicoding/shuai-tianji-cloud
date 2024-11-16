package com.shuai.auth.service.impl;

import com.shuai.api.client.user.UserClient;
import com.shuai.api.dto.user.LoginFormDTO;
import com.shuai.auth.service.IAccountService;
import com.shuai.auth.service.ILoginRecordService;
import com.shuai.auth.util.JwtTool;
import com.shuai.common.constants.ErrorInfo;
import com.shuai.common.constants.JwtConstants;
import com.shuai.common.domain.R;
import com.shuai.common.domain.dto.LoginUserDTO;
import com.shuai.common.exceptions.OpenFeignException;
import com.shuai.common.utils.BooleanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 账号表，平台内所有用户的账号、密码信息 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final JwtTool jwtTool;
    private final UserClient userClient;
    private final ILoginRecordService loginRecordService;

    @Override
    public String login(LoginFormDTO loginDTO, boolean isStaff) throws UnsupportedEncodingException {
        // 1.查询并校验用户信息
        R<LoginUserDTO> userDetail = userClient.queryUserDetail(loginDTO, isStaff);
        if (userDetail.getCode() != ErrorInfo.Code.SUCCESS) {
            throw new OpenFeignException(userDetail.getMsg());
        }
        LoginUserDTO detail = userDetail.getData();
        // 2.基于JWT生成登录token
        // 2.1.设置记住我标记
        detail.setRememberMe(loginDTO.getRememberMe());
        // 2.2.生成token
        String token = generateToken(detail);
        // 3.计入登录信息表
        loginRecordService.loginSuccess(loginDTO.getCellPhone(), detail.getUserId());
        // 4.返回结果
        return token;
    }

    private String generateToken(LoginUserDTO detail) throws UnsupportedEncodingException {
        // 2.2.生成access-token
        String token = jwtTool.createToken(detail);
        // 2.3.生成refresh-token，将refresh-token的JTI 保存到Redis
        String refreshToken = jwtTool.createRefreshToken(detail);

        // 2.4.将refresh-token写入用户cookie，并设置HttpOnly为true
        int maxAge = BooleanUtils.isTrue(detail.getRememberMe()) ? (int) JwtConstants.JWT_REMEMBER_ME_TTL.getSeconds() : -1;
        // WebUtils.cookieBuilder()
        //         .name(detail.getRoleId() == 2 ? JwtConstants.REFRESH_HEADER : JwtConstants.ADMIN_REFRESH_HEADER)
        //         .value(refreshToken)
        //         .maxAge(maxAge)
        //         .httpOnly(true)  // 设置为true,则浏览器不会允许JavaScript 访问此 Cookie，增加安全性。
        //         .build();
        return token;
    }

    @Override
    public void logout() throws UnsupportedEncodingException {
        // 删除jti
        jwtTool.cleanJtiCache();
        // 删除cookie
        // WebUtils.cookieBuilder()
        //         .name(JwtConstants.REFRESH_HEADER)
        //         .value("")
        //         .maxAge(0)
        //         .httpOnly(true)
        //         .build();

    }

    @Override
    public String refreshToken(String refreshToken) throws UnsupportedEncodingException {
        // 1.校验refresh-token,校验JTI
        LoginUserDTO userDTO = jwtTool.parseRefreshToken(refreshToken);
        // 2.生成新的access-token、refresh-token
        return generateToken(userDTO);
    }
}
