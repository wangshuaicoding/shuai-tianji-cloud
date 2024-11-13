package com.shuai.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.api.dto.auth.RoleDTO;
import com.shuai.auth.domain.po.Role;
import com.shuai.common.domain.query.PageQuery;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
public interface IRoleService extends IService<Role> {

    boolean exists(Long roleId);
    boolean exists(List<Long> roleIds);

    void deleteRole(Long id);

    List<Role> pageQueryList(PageQuery pageQuery);
}
