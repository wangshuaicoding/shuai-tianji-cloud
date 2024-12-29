package com.shuai.auth.handler;

import com.shuai.auth.service.IPointsBoardSeasonService;
import com.shuai.auth.service.IPointsBoardService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 定时任务生成榜单表
 */
@Component
@RequiredArgsConstructor
public class PointsBoardPersistentHandler {

    private final IPointsBoardService pointsBoardService;
    private final IPointsBoardSeasonService pointsBoardSeasonService;

    // @Scheduled(cron = "0 0 3 1 * ?") // 每月1号，凌晨3点执行
    @XxlJob("createTableJob")
    public void createPointsBoardTableOfLastSeason() {
        // 1、获取上月时间
        LocalDateTime time = LocalDateTime.now().minusMonths(1);
        // 2、查询赛季id
        Integer season = pointsBoardSeasonService.querySeasonByTime(time);
        if (season == null) {
            return;
        }
        // 3、创建表
        pointsBoardService.createPointsBoardTable(season);
    }
}
