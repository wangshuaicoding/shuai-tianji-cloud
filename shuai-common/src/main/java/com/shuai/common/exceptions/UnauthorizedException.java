package com.shuai.common.exceptions;

import lombok.Getter;

// 未登录异常，对应401 2024/6/21 下午 13:34 By 少帅
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
