package com.shuai.user.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.enums.UserType;
import com.shuai.common.utils.BeanUtils;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.RandomUtils;
import com.shuai.user.constants.UserConstants;
import com.shuai.user.domain.dto.StudentFormDTO;
import com.shuai.user.domain.po.User;
import com.shuai.user.domain.po.UserDetail;
import com.shuai.user.domain.query.UserPageQuery;
import com.shuai.user.domain.vo.StudentPageVo;
import com.shuai.user.service.IStudentService;
import com.shuai.user.service.IUserDetailService;
import com.shuai.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学员详情表 服务实现类
 * </p>
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final IUserService userService;
    private final IUserDetailService detailService;

    @Override
    @Transactional
    public void saveStudent(StudentFormDTO studentFormDTO) {
        // 1.新增用户账号
        User user = new User();
        user.setCellPhone(studentFormDTO.getCellPhone());
        user.setPassword(studentFormDTO.getPassword());
        user.setType(UserType.STUDENT);
        userService.addUserByPhone(user, studentFormDTO.getCode());

        // 2.新增学员详情
        UserDetail student = new UserDetail();
        student.setId(user.getId());
        student.setName(RandomUtils.randomString(8));
        student.setRoleId(UserConstants.STUDENT_ROLE_ID);
        detailService.save(student);
    }

    @Override
    public void updateMyPassword(StudentFormDTO studentFormDTO) {
        userService.updatePasswordByPhone(
                studentFormDTO.getCellPhone(), studentFormDTO.getCode(), studentFormDTO.getPassword()
        );
    }

    @Override
    public PageDTO<StudentPageVo> queryStudentPage(UserPageQuery query) {
        // 1.分页条件
        Page<UserDetail> page  =  detailService.queryUserDetailByPage(query, UserType.STUDENT);
        List<UserDetail> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        // 2.查询购买的课程数量
        List<Long> stuIds = records.stream().map(UserDetail::getId).collect(Collectors.toList());
        // Map<Long, Integer> numMap = tradeClient.countEnrollCourseOfStudent(stuIds);

        // 3.处理vo
        List<StudentPageVo> list = new ArrayList<>(records.size());
        for (UserDetail r : records) {
            StudentPageVo v = BeanUtils.toBean(r, StudentPageVo.class);
            list.add(v);
            // v.setCourseAmount(numMap.get(r.getId()));
        }
        return new PageDTO<>(page.getTotal(), page.getPages(), list);
    }
}
