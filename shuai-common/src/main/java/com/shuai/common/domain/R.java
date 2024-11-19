package com.shuai.common.domain;

import com.shuai.common.constants.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.MDC;

import java.io.Serializable;

import static com.shuai.common.constants.ErrorInfo.Code.FAILED;
import static com.shuai.common.constants.ErrorInfo.Code.SUCCESS;
import static com.shuai.common.constants.ErrorInfo.Message.OK;

@Data
@ApiModel(description = "通用响应结果")
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务状态码，200-成功，其它-失败")
    private int code;
    @ApiModelProperty(value = "响应消息", example = "OK")
    private String message;
    @ApiModelProperty(value = "响应数据")
    private T data;

    public static R<Void> ok() {
        return new R<>(SUCCESS, OK, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, OK, data);
    }

    public static <T> R<T> error(String message) {
        return new R<>(FAILED, message, null);
    }

    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message, null);
    }

    public R() {
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean success(){
        return code == SUCCESS;
    }

}
