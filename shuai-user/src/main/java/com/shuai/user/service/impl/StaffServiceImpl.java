package com.shuai.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuai.api.cache.RoleCache;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.enums.UserType;
import com.shuai.common.utils.BeanUtils;
import com.shuai.user.domain.po.UserDetail;
import com.shuai.user.domain.query.UserPageQuery;
import com.shuai.user.domain.vo.StaffVO;
import com.shuai.user.service.IStaffService;
import com.shuai.user.service.IUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工详情表 服务实现类
 * </p>
 *
 */
@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements IStaffService {

    private final IUserDetailService detailService;
    private final RoleCache roleCache;

    @Override
    public PageDTO<StaffVO> queryStaffPage(UserPageQuery query) {
        // 1.搜索
        Page<UserDetail> p = detailService.queryUserDetailByPage(query, UserType.STAFF);
        // 2.处理vo
        return PageDTO.of(p, u -> {
            StaffVO v = BeanUtils.toBean(u, StaffVO.class);
            v.setRoleName(roleCache.getRoleName(u.getRoleId()));
            return v;
        });
    }
}
