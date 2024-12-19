package com.shuai.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.message.domain.po.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程老师关系表草稿 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-19
 */
public interface ICourseTeacherService extends IService<CourseTeacher> {

    List<CourseTeacher> getCourseTeacherById(Long id);
}
