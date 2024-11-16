package com.shuai.common.exceptions;

import lombok.Getter;

// 服务器理解请求客户端的请求，但是拒绝执行此请求。
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
