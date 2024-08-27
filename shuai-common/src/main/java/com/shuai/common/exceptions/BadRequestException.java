package com.shuai.common.exceptions;

import lombok.Getter;

// 請求参数异常，对应状态码400 2024/6/21 上午 11:29 By 少帅
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
