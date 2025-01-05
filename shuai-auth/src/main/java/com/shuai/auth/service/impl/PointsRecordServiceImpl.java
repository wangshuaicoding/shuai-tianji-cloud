package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.auth.constants.PointsRecordConstants;
import com.shuai.auth.constants.RedisConstants;
import com.shuai.auth.domain.po.PointsRecord;
import com.shuai.auth.domain.vo.PointsRecordVO;
import com.shuai.auth.enums.PointsRecordType;
import com.shuai.auth.mapper.PointsRecordMapper;
import com.shuai.auth.service.IPointsRecordService;
import com.shuai.common.exceptions.DbException;
import com.shuai.common.utils.CollUtils;
import com.shuai.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学习积分记录，每个月底清零 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-24
 */
@Service
@RequiredArgsConstructor
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements IPointsRecordService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void addPointsRecord(Long userId, Integer points, PointsRecordType type) {
        int maxPoints = type.getMaxPoints();
        LocalDateTime now = LocalDateTime.now();
        // 判断当前积分方式有没有上限
        int realPoints = points;
        if (maxPoints > 0) {
            LocalDateTime start = DateUtils.getDayStartTime(now);
            LocalDateTime end = DateUtils.getDayEndTime(now);
            LambdaQueryWrapper<PointsRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .ge(PointsRecord::getCreateTime, start)
                    .lt(PointsRecord::getCreateTime, end)
                    .eq(PointsRecord::getType, type)
                    .eq(PointsRecord::getUserId, userId);
            Integer currentPoints = baseMapper.queryUserPointsByTypeAndDate(queryWrapper);
            if (currentPoints >= maxPoints) {
                return;
            }

            // 没有超过，保存积分记录
            if (currentPoints + points > maxPoints) {
                realPoints = maxPoints - currentPoints;
            }
        }
        PointsRecord pointsRecord = new PointsRecord();
        pointsRecord.setUserId(userId);
        pointsRecord.setPoints(realPoints);
        pointsRecord.setType(type);
        pointsRecord.setCreateTime(now);
        boolean success = save(pointsRecord);
        if (!success) {
            throw new DbException(PointsRecordConstants.ADD_POINTS_RECORD_ERROR);
        }

        // 更新总积分到redis中
        String key = RedisConstants.POINTS_BOARD_KEY_PREFIX + now.format(DateUtils.POINTS_BOARD_SUFFIX_FORMATTER);
        redisTemplate.opsForZSet().incrementScore(key, userId.toString(), realPoints);
    }

    @Override
    public List<PointsRecordVO> queryMyTodayPoints() {
        // 1、获取用户
        // Long userId = UserContext.getUser();
        Long userId = 2L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = DateUtils.getDayStartTime(now);
        LocalDateTime end = DateUtils.getDayEndTime(now);

        List<PointsRecord> recordList = lambdaQuery()
                .eq(PointsRecord::getUserId, userId)
                .between(PointsRecord::getCreateTime, start, end)
                .list();
        if (CollUtils.isEmpty(recordList)) {
            return null;
        }
        Map<PointsRecordType, Integer> pMap = recordList.stream().collect(Collectors.groupingBy(PointsRecord::getType, Collectors.summingInt(PointsRecord::getPoints)));
        List<PointsRecordVO> result = new ArrayList<>(pMap.size());
        for (Map.Entry<PointsRecordType, Integer> entry : pMap.entrySet()) {
            PointsRecordVO vo = new PointsRecordVO();
            vo.setType(entry.getKey().getDesc());
            vo.setPoints(entry.getValue());
            vo.setMaxPoints(entry.getKey().getMaxPoints());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<Integer> queryMySignRecord() {
        // Long userId = UserContext.getUser();
        long userId = 2L;
        LocalDateTime now = LocalDateTime.now();
        int len = now.getDayOfMonth();
        // 拼接key
        String key = RedisConstants.SIGN_RECORD_KEY_PREFIX
                + userId
                + now.format(DateUtils.SIGN_DATE_SUFFIX_FORMATTER);

        List<Long> list = redisTemplate.opsForValue()
                .bitField(key, BitFieldSubCommands.create().get(
                        BitFieldSubCommands.BitFieldType.unsigned(len)).valueAt(0));

        if (CollUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<Integer> result = new ArrayList<>(len);
        int num = list.get(0).intValue();
        for (int i = 0; i < len; i++) {
            result.add(num & 1);
            num >>>= 1;
        }
        Collections.reverse(result);
        return result;
    }
}
