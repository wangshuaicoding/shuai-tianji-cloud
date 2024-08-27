package com.shuai.common.exceptions;

import lombok.Getter;

// 权限异常，对应403 2024/6/21 上午 11:29 By 少帅
@Getter
public class ForbiddenException extends CommonException {
    private final int status = 403;

    public ForbiddenException(String message) {
        super(403, message);
    }

    public ForbiddenException(int code, String message) {
        super(code, message);
    }

    public ForbiddenException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
