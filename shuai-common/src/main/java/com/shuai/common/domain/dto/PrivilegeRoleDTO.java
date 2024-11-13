package com.shuai.common.domain.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PrivilegeRoleDTO {
    /**
     * 权限id
     */
    private Long id;
    /**
     * 请求路径
     */
    private String antPath;
    /**
     * 是否是内部接口
     */
    private Boolean internal;
    /**
    * 角色id
    */
    private Set<Long> roles;
}
