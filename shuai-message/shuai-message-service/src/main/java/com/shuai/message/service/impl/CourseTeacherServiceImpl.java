package com.shuai.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.message.domain.po.CourseTeacher;
import com.shuai.message.mapper.CourseTeacherMapper;
import com.shuai.message.service.ICourseTeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程老师关系表草稿 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-19
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {

    @Override
    public List<CourseTeacher> getCourseTeacherById(Long id) {
        return lambdaQuery()
                .eq(CourseTeacher::getCourseId, id)
                .list();
    }
}
