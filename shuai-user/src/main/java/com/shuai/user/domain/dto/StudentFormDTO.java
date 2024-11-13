package com.shuai.user.domain.dto;

import com.shuai.common.validate.annotations.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(description = "学生注册和修改密码的表单实体")
public class StudentFormDTO {

    @ApiModelProperty(value = "手机号", example = "13800010004")
    @Mobile
    @NotEmpty(message = "手机号不能为空")
    private String cellPhone;

    @ApiModelProperty(value = "密码", example = "123456")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码", example = "645632")
    private String code;
}
