package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.auth.domain.po.LearningLesson;
import com.shuai.auth.mapper.LearningLessonMapper;
import com.shuai.auth.service.ILearningLessonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生课程表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
@Service
public class LearningLessonServiceImpl extends ServiceImpl<LearningLessonMapper, LearningLesson> implements ILearningLessonService {

    @Override
    public void addUserLessons(Long userId, List<Long> courseIds) {

    }
}
