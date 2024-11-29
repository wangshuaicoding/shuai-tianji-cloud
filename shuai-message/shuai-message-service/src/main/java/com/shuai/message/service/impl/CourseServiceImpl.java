package com.shuai.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.message.domain.po.Course;
import com.shuai.message.mapper.CourseMapper;
import com.shuai.message.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 草稿课程 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Override
    public List<CourseSimpleInfoDTO> selectListByIds(List<Long> ids) {
        List<Course> courseList = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(courseList)) {
            return null;
        }
        return BeanUtil.copyToList(courseList, CourseSimpleInfoDTO.class);
    }
}
