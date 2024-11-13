package com.shuai.auth.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuai.auth.domain.po.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectPageQuery(Page<Role> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<Role> queryWrapper);
}
