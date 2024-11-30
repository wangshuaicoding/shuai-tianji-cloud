package com.shuai.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.po.LearningLesson;
import com.shuai.auth.domain.vo.LearningLessonVO;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.domain.query.PageQuery;

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

    PageDTO queryLessonPage(PageQuery pageQuery);

    LearningLessonVO checkMyRecentCourse();
}
