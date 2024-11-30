package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.client.course.CourseClient;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.auth.domain.po.LearningLesson;
import com.shuai.auth.mapper.LearningLessonMapper;
import com.shuai.auth.service.ILearningLessonService;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.domain.query.PageQuery;
import com.shuai.common.utils.CollUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
@RequiredArgsConstructor
@Slf4j
public class LearningLessonServiceImpl extends ServiceImpl<LearningLessonMapper, LearningLesson> implements ILearningLessonService {

    private final CourseClient courseClient;

    @Override
    @Transactional
    public void addUserLessons(Long userId, List<Long> courseIds) {
        // 查询课程的过期时间
        List<CourseSimpleInfoDTO> courseSimpleInfoList = courseClient.selectListByIds(courseIds);
        if (CollUtils.isEmpty(courseSimpleInfoList)) {
            log.error("课程信息不存在，无法加入课表");
            return;
        }
        List<LearningLesson> result = new ArrayList<>(courseSimpleInfoList.size());
        for (CourseSimpleInfoDTO infoDTO : courseSimpleInfoList) {
            LearningLesson learningLesson = new LearningLesson();

            Integer validDuration = infoDTO.getValidDuration();
            if (validDuration != null && validDuration > 0) {
                learningLesson.setExpireTime(LocalDateTime.now().plusMonths(validDuration));
            }

            learningLesson.setUserId(userId);
            learningLesson.setCourseId(infoDTO.getId());
            result.add(learningLesson);
        }

        saveBatch(result);
    }

    @Override
    public PageDTO queryLessonPage(PageQuery pageQuery) {
        Long userId = 2L;
        Page<LearningLesson> page = lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .page(pageQuery.toMpPage(pageQuery.getSortBy(),pageQuery.getIsAsc()));
        List<LearningLesson> records = page.getRecords();

        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        return PageDTO.of(page);
    }
}
