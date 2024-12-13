package com.shuai.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.client.course.CatalogueClient;
import com.shuai.api.client.course.CourseClient;
import com.shuai.api.dto.course.CataSimpleInfoDTO;
import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.api.dto.course.CourseSimpleInfoDTO;
import com.shuai.auth.constants.CourseConstants;
import com.shuai.auth.domain.po.LearningLesson;
import com.shuai.auth.domain.vo.LearningLessonVO;
import com.shuai.auth.enums.LessonStatus;
import com.shuai.auth.mapper.LearningLessonMapper;
import com.shuai.auth.service.ILearningLessonService;
import com.shuai.common.domain.dto.PageDTO;
import com.shuai.common.domain.query.PageQuery;
import com.shuai.common.exceptions.BadRequestException;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.UserContext;
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
    private final CatalogueClient catalogueClient;

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

    @Override
    public LearningLessonVO checkMyRecentCourse() {
        Long userId = 2L;
        LearningLesson learningLesson = lambdaQuery().eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getStatus, LessonStatus.LEARNING)
                .orderByDesc(LearningLesson::getLatestLearnTime)
                .last("limit 1")
                .one();
        if (learningLesson == null) {
            return null;
        }

        LearningLessonVO vo = BeanUtil.copyProperties(learningLesson, LearningLessonVO.class);
        // 获取课程信息
        CourseFullInfoDTO courseFullInfo = courseClient.selectFullInfoById(learningLesson.getCourseId());
        if (courseFullInfo == null) {
            throw new BadRequestException(CourseConstants.COURSE_NOT_EXIST);
        }
        vo.setCourseName(courseFullInfo.getName());
        vo.setCoverUrl(courseFullInfo.getCoverUrl());
        vo.setSectionNum(courseFullInfo.getSectionNum());

        // 统计课程表中的课程数量
        Integer courseCount = lambdaQuery().eq(LearningLesson::getUserId, userId).count();
        vo.setCourseAmount(courseCount);

        // 查询小节信息
        List<CataSimpleInfoDTO> cataSimpleInfoList = catalogueClient.batchQuery(CollUtils.singletonList(learningLesson.getLatestSectionId()));
        if (!CollUtils.isEmpty(cataSimpleInfoList)) {
            CataSimpleInfoDTO cataSimpleInfo = cataSimpleInfoList.get(0);
            vo.setLatestSectionName(cataSimpleInfo.getName());
            vo.setLatestSectionIndex(cataSimpleInfo.getCIndex());
        }
        return vo;
    }

    @Override
    public Integer queryLearningStatus(Long courseId) {
        Long userId = UserContext.getUser();

        LearningLesson learningLesson = lambdaQuery().eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getCourseId, courseId)
                .one();
        if (learningLesson == null) {
            throw new BadRequestException(CourseConstants.USER_NOT_COURSE);
        }
        return learningLesson.getStatus().getValue();
    }

    @Override
    public Boolean removeLearningLessonById(Long courseId) {
        // Long userId = UserContext.getUser();
        Long userId = 8888L;
        return lambdaUpdate().eq(LearningLesson::getCourseId, courseId)
                .eq(LearningLesson::getUserId, userId)
                .remove();
    }

    @Override
    public Long checkCourseValid(Long courseId) {
        Long userId = UserContext.getUser();
        LearningLesson learningLesson = lambdaQuery().eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getCourseId, courseId)
                .one();
        if (learningLesson == null) {
            return null;
        }
        return learningLesson.getId();
    }

    @Override
    public Integer countLearningUser(Long courseId) {
        return lambdaQuery().eq(LearningLesson::getCourseId, courseId).count();
    }
}
