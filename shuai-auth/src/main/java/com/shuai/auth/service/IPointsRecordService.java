package com.shuai.auth.service;

import com.shuai.auth.domain.po.PointsRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shuai.auth.domain.vo.PointsRecordVO;
import com.shuai.auth.enums.PointsRecordType;

import java.util.List;

/**
 * <p>
 * 学习积分记录，每个月底清零 服务类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
public interface IPointsRecordService extends IService<PointsRecord> {

    void addPointsRecord(Long userId, Integer points, PointsRecordType pointsRecordType);

    List<PointsRecordVO> queryMyTodayPoints();

    List<Integer> queryMySignRecord();
}
