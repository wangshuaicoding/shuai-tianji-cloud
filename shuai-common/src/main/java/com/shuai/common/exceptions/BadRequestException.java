package com.shuai.common.exceptions;

import lombok.Getter;

// 请求语法错误或请求参数无效 2024/11/16 10:59 By 少帅
@Getter
public class BadRequestException extends CommonException{
    private final int status = 400;

    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException(int code, String message) {
        super(code, message);
    }

    public BadRequestException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
