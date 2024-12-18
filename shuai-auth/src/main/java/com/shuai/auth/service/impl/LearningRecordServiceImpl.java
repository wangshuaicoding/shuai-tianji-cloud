package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.api.client.course.CourseClient;
import com.shuai.api.dto.course.CourseFullInfoDTO;
import com.shuai.auth.constants.CourseConstants;
import com.shuai.auth.constants.LearningRecordConstants;
import com.shuai.auth.constants.LessonConstants;
import com.shuai.auth.domain.dto.LearningRecordDTO;
import com.shuai.auth.domain.dto.LearningRecordFormDTO;
import com.shuai.auth.domain.po.LearningLesson;
import com.shuai.auth.domain.po.LearningRecord;
import com.shuai.auth.domain.vo.LearningProgressVO;
import com.shuai.auth.enums.LessonStatus;
import com.shuai.auth.enums.SectionTypeStatus;
import com.shuai.auth.mapper.LearningRecordMapper;
import com.shuai.auth.service.ILearningLessonService;
import com.shuai.auth.service.ILearningRecordService;
import com.shuai.auth.util.LearningRecordDelayTaskHandler;
import com.shuai.common.exceptions.BadRequestException;
import com.shuai.common.exceptions.DbException;
import com.shuai.common.exceptions.OpenFeignException;
import com.shuai.common.utils.BeanUtils;
import com.shuai.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 学习记录表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-14
 */
@Service
@RequiredArgsConstructor
public class LearningRecordServiceImpl extends ServiceImpl<LearningRecordMapper, LearningRecord> implements ILearningRecordService {

    private final ILearningLessonService learningLessonService;

    private final CourseClient courseClient;
    private final LearningRecordDelayTaskHandler taskHandler;

    @Override
    public LearningProgressVO queryLearningRecord(Long courseId) {
        // Long userId = UserContext.getUser();
        Long userId = 2L;

        LearningLesson learningLesson = learningLessonService.lambdaQuery()
                .eq(LearningLesson::getUserId, userId)
                .eq(LearningLesson::getCourseId, courseId)
                .one();
        if (learningLesson == null) {
            throw new BadRequestException(CourseConstants.USER_NOT_COURSE);
        }

        List<LearningRecord> list = lambdaQuery()
                .eq(LearningRecord::getLessonId, learningLesson.getId()).list();

        LearningProgressVO vo = new LearningProgressVO();
        vo.setId(learningLesson.getId());
        vo.setLatestSectionId(learningLesson.getLatestSectionId());
        vo.setRecords(BeanUtils.copyList(list, LearningRecordDTO.class));

        return vo;
    }

    @Override
    @Transactional
    public void saveLearningRecord(LearningRecordFormDTO formDTO) {
        // Long userId = UserContext.getUser();
        Long userId = 2L;

        Boolean finished = false;
        if (formDTO.getSectionType() == SectionTypeStatus.VIDEO.getValue()) {
            // 处理视频
            finished = handleVideo(formDTO, userId);
        } else {
            // 处理考试
            finished = handleExam(formDTO, userId);
        }

        if (!finished) {
            // 没有新学完的小节，无需更新课表
            return;
        }

        LearningLesson lesson = learningLessonService.getById(formDTO.getLessonId());
        if (lesson == null) {
            throw new BadRequestException(LessonConstants.LESSON_NOT_EXIST);
        }

        // 判断是否有新的完成小节
        boolean flag = false;
        CourseFullInfoDTO fullInfoDTO = courseClient.selectFullInfoById(lesson.getCourseId());
        if (fullInfoDTO == null) {
            throw new OpenFeignException(CourseConstants.COURSE_NOT_EXIST);
        }
        flag = lesson.getLearnedSections() + 1 >= fullInfoDTO.getSectionNum();

        learningLessonService.lambdaUpdate()
                .set(flag, LearningLesson::getStatus, LessonStatus.COMPLETED)
                .setSql( "learned_sections = learned_sections + 1")
                .eq(LearningLesson::getId,lesson.getId())
                .update();
    }

    private Boolean handleExam(LearningRecordFormDTO formDTO, Long userId) {
        LearningRecord learningRecord = BeanUtils.copyBean(formDTO, LearningRecord.class);
        learningRecord.setUserId(userId);
        learningRecord.setFinishTime(formDTO.getCommitTime());
        learningRecord.setFinished(true);
        boolean success = save(learningRecord);
        if (!success) {
            throw new DbException(LearningRecordConstants.FAILED_SAVE_EXAMS_RECORDS);
        }
        return true;
    }

    private Boolean handleVideo(LearningRecordFormDTO formDTO, Long userId) {

        LearningRecord old = queryOldRecord(formDTO.getLessonId(),formDTO.getSectionId());
        if (old == null) {
            // 没有学习过，添加学习记录
            LearningRecord learningRecord = BeanUtils.copyBean(formDTO, LearningRecord.class);
            learningRecord.setUserId(userId);
            learningRecord.setFinishTime(formDTO.getCommitTime());
            boolean success = save(learningRecord);
            if (!success) {
                throw new DbException(LearningRecordConstants.FAILED_SAVE_LEARNING_RECORDS);
            }
            return false;
        }
        // 判断是否是第一次完成
        boolean finished = !old.getFinished() && formDTO.getMoment() >= formDTO.getDuration() >> 1;
        if (!finished) {
            LearningRecord record = new LearningRecord();
            record.setLessonId(formDTO.getLessonId());
            record.setSectionId(formDTO.getSectionId());
            record.setMoment(formDTO.getMoment());
            record.setId(old.getId());
            record.setFinished(old.getFinished());
            taskHandler.addLearningRecordTask(record);
            return false;
        }
        boolean success = lambdaUpdate()
                .set(LearningRecord::getMoment, formDTO.getMoment())
                .set(LearningRecord::getFinishTime, formDTO.getCommitTime())
                .set(LearningRecord::getFinished, true)
                .eq(LearningRecord::getId, old.getId())
                .update();
        if (!success) {
            throw new DbException(LearningRecordConstants.FAILED_UPDATE_LEARNING_RECORDS);
        }
        // 清除缓存
        taskHandler.cleanRecordCache(formDTO.getLessonId(),formDTO.getSectionId());
        return true;
    }

    private LearningRecord queryOldRecord(Long lessonId, Long sectionId) {
        // 1、查询缓存
        LearningRecord record = taskHandler.readRecordCache(lessonId, sectionId);
        if (record != null) {
            return record;
        }

        // 未命中，查询数据库
        record = lambdaQuery().eq(LearningRecord::getLessonId, lessonId)
                .eq(LearningRecord::getSectionId, sectionId)
                .one();
        // 写入缓存
        if (record != null) {
            taskHandler.writeRecordCache(record);
        }
        return record;
    }

}
