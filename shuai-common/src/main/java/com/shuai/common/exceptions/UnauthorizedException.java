package com.shuai.common.exceptions;

import lombok.Getter;

// 请求要求用户的身份认证 2024/11/16 10:59 By 少帅
@Getter
public class UnauthorizedException extends CommonException {
    private final int status = 401;

    public UnauthorizedException(String message) {
        super(401, message);
    }

    public UnauthorizedException(int code, String message) {
        super(code, message);
    }

    public UnauthorizedException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
