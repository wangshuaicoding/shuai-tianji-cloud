package com.shuai.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.auth.domain.po.PointsBoardSeason;
import com.shuai.auth.mapper.PointsBoardSeasonMapper;
import com.shuai.auth.service.IPointsBoardSeasonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 赛季表 服务实现类
 * </p>
 *
 * @author Shuai
 * @since 2024-12-26
 */
@Service
public class PointsBoardSeasonServiceImpl extends ServiceImpl<PointsBoardSeasonMapper, PointsBoardSeason> implements IPointsBoardSeasonService {

    @Override
    public Integer querySeasonByTime(LocalDateTime time) {
        // 方式一：
        // PointsBoardSeason one = lambdaQuery()
        //         .ge(PointsBoardSeason::getBeginTime, time)
        //         .le(PointsBoardSeason::getEndTime, time)
        //         .one();
        // if (one == null) {
        //     throw new DbException(PointsBoardSeasonConstants.QUERY_SEASON_RETURNS_EMPTY);
        // }
        // return one.getId();

        // 方式二：
        Optional<PointsBoardSeason> opt = lambdaQuery()
                .ge(PointsBoardSeason::getBeginTime, time)
                .le(PointsBoardSeason::getEndTime, time)
                .oneOpt();
        return opt.map(PointsBoardSeason::getId).orElse(null);
    }
}
