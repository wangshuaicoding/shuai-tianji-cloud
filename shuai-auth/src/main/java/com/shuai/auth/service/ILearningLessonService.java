package com.shuai.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.po.LearningLesson;

import java.util.List;

/**
 * <p>
 * 学生课程表 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-11-29
 */
public interface ILearningLessonService extends IService<LearningLesson> {

    void addUserLessons(Long userId, List<Long> courseIds);
}
