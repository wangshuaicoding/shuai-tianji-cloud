package com.shuai.common.domain.dto;

import lombok.Data;

@Data
public class LoginUserDTO {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 记住我
     */
    private Boolean rememberMe;
}
