package com.shuai.auth.service;

import com.shuai.auth.domain.dto.LearningRecordFormDTO;
import com.shuai.auth.domain.po.LearningRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.vo.LearningProgressVO;

/**
 * <p>
 * 学习记录表 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-14
 */
public interface ILearningRecordService extends IService<LearningRecord> {

    LearningProgressVO queryLearningRecord(Long courseId);

    void saveLearningRecord(LearningRecordFormDTO formDTO);
}
