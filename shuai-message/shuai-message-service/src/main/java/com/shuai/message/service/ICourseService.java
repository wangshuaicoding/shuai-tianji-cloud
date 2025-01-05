package com.shuai.message.service;

import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.message.domain.po.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 草稿课程 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
public interface ICourseService extends IService<Course> {

    List<CourseSimpleInfoDTO> selectListByIds(List<Long> ids);

    CourseFullInfoDTO selectFullInfoById(Long id);
}
