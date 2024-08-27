package com.shuai.common.exceptions;

/**
 * 业务不合法异常，业务逻辑异常，对应500
 **/
public class BizIllegalException extends CommonException {
    public BizIllegalException(String message) {
        super(message);
    }

    public BizIllegalException(int code, String message) {
        super(code, message);
    }

    public BizIllegalException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
