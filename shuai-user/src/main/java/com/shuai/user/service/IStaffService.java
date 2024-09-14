package com.shuai.user.service;

import com.shuai.common.domain.dto.PageDTO;
import com.shuai.user.domain.query.UserPageQuery;
import com.shuai.user.domain.vo.StaffVO;

/**
 * <p>
 * 员工详情表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-12
 */
public interface IStaffService {
    PageDTO<StaffVO> queryStaffPage(UserPageQuery pageQuery);
}
