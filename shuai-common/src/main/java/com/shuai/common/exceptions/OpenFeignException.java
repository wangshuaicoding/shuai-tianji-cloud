package com.shuai.common.exceptions;

import lombok.Getter;

// 服务器理解请求客户端的请求，但是拒绝执行此请求。
@Getter
public class OpenFeignException extends CommonException {
    private final int status = 518;

    public OpenFeignException(String message) {
        super(518, message);
    }

    public OpenFeignException(int code, String message) {
        super(code, message);
    }

    public OpenFeignException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
