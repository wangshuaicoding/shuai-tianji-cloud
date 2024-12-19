package com.shuai.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.common.exceptions.DbException;
import com.shuai.message.constants.CourseConstants;
import com.shuai.message.domain.po.Course;
import com.shuai.message.domain.po.CourseTeacher;
import com.shuai.message.mapper.CourseMapper;
import com.shuai.message.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.message.service.ICourseTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 草稿课程 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    private final ICourseTeacherService teacherService;
    @Override
            public List<CourseSimpleInfoDTO> selectListByIds(List<Long> ids) {
                List<Course> courseList = baseMapper.selectBatchIds(ids);
                if (CollectionUtil.isEmpty(courseList)) {
                    return null;
        }
        return BeanUtil.copyToList(courseList, CourseSimpleInfoDTO.class);
    }

    @Override
    public CourseFullInfoDTO selectFullInfoById(Long id) {
        Course course = baseMapper.selectById(id);
        if (course == null) {
            throw new DbException(CourseConstants.COURSE_NOT_EXIST);
        }
        CourseFullInfoDTO dto = BeanUtil.copyProperties(course, CourseFullInfoDTO.class);

        // 查询课程老师表中的数据
        List<CourseTeacher> courseTeacherList = teacherService.getCourseTeacherById(id);
        if (CollectionUtil.isNotEmpty(courseTeacherList)) {
            List<Long> teacherIds = courseTeacherList.stream().map(CourseTeacher::getTeacherId).collect(Collectors.toList());
            dto.setTeacherIds(teacherIds);
        }
        return dto;
    }
}
