package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.dto.auth.RoleDTO;
import com.shuai.auth.mapper.RoleMapper;
import com.shuai.auth.service.IRoleMenuService;
import com.shuai.auth.service.IRolePrivilegeService;
import com.shuai.auth.service.IRoleService;
import com.shuai.auth.util.PrivilegeCache;
import com.shuai.common.domain.query.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shuai.auth.domain.po.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final IRoleMenuService roleMenuService;
    private final IRolePrivilegeService rolePrivilegeService;
    private final PrivilegeCache privilegeCache;
    private final RoleMapper roleMapper;

    @Override
    public boolean exists(Long roleId) {
        Integer count = lambdaQuery().eq(Role::getId, roleId).count();
        return count > 0;
    }

    @Override
    public boolean exists(List<Long> roleIds) {
        Integer count = lambdaQuery().in(Role::getId, roleIds).count();
        return count != roleIds.size();
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        // 1.删除角色
        removeById(id);
        // 2.删除角色与权限的关联信息
        roleMenuService.removeByRoleId(id);
        rolePrivilegeService.removeByRoleId(id);
        // 3.清理缓存
        privilegeCache.removeCacheByRoleId(id);
    }

    @Override
    public List<Role> pageQueryList(PageQuery pageQuery) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        return roleMapper.selectPageQuery(new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()),queryWrapper);
    }
}
